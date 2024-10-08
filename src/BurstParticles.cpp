#include "BurstParticles.h"

#include "RNG.h"

#include <algorithm>
#include <iostream>

void BurstParticle::simulate(Number delta_time, Number aspect_ratio)
{
	velocity.y += delta_time * 4.0L;
	//velocity.y = 0;
	//velocity.x = 0;
	Vec2 new_pos = velocity * delta_time;
	new_pos.y *= aspect_ratio;
	new_pos.y += delta_time * 2.0;
	position += new_pos;
	angle = glm::atan(velocity.x, velocity.y);
}

bool BurstParticleGroup::is_dead() const
{
	return total_time >= lifetime;
}

uint8_t BurstParticleGroup::get_alpha() const
{
	return std::clamp((1.0L - total_time / lifetime) * 255.0L, 0.0L, 255.0L) / 1.25L; //1.1 -> 1.25
}

void BurstParticleGroup::simulate(Number delta_time, Number aspect_ratio)
{
	total_time += delta_time;
	for (auto& particle : _particles)
	{
		particle.simulate(delta_time, aspect_ratio);
	}
}

BurstParticles::BurstParticles(Renderer* renderer, const std::string& texture_name)
	:_texture{renderer, texture_name},
	_renderer{renderer}
{
	_texture.blend_mode = 1;
}

void BurstParticles::add(Vec2 pos, Vec2 size, uint16_t number_of_particles, Number max_lifetime, Vec2 min_velocity, Vec2 max_velocity, Color color)
{
	auto& group = _particle_groups.emplace_back(BurstParticleGroup{ color, max_lifetime * 0.25L });
	for (uint16_t i = 0; i < number_of_particles; ++i)
	{
		group._particles.emplace_back(BurstParticle{
			{RNG::number(pos.x - size.x,pos.x + size.x),RNG::number(pos.y - size.y,pos.y + size.y)},
			{RNG::number(min_velocity.x,max_velocity.x),RNG::number(min_velocity.y,max_velocity.y)}
			});
	}
}

void BurstParticles::update(Number delta_time)
{
	if (!enabled) { return; }
	//remove old particle groups
	_particle_groups.erase(std::remove_if(_particle_groups.begin(), _particle_groups.end(), [&](const BurstParticleGroup& particle_group) { return particle_group.is_dead(); }), _particle_groups.end());

	for (auto& particle_group : _particle_groups)
	{
		particle_group.simulate(delta_time, _renderer->get_aspect_ratio());
	}
}

void BurstParticles::render() const
{
	Color previous_tint = _texture.tint;
	if (!enabled) { return; }
	for (const auto& particle_group : _particle_groups)
	{
		_texture.tint = particle_group.color;
		_texture.tint.a = uint16_t(particle_group.color.a) * uint16_t(particle_group.get_alpha()) / 255;
		_texture.tint.r *= Number(previous_tint.r) / 255.0L;
		_texture.tint.g *= Number(previous_tint.g) / 255.0L;
		_texture.tint.b *= Number(previous_tint.b) / 255.0L;
		const Number size = (1.0L-particle_group.total_time/particle_group.lifetime) * 0.125L;
		for (const auto& particle : particle_group._particles)
		{
			_renderer->render(&_texture, { 0,0 }, _texture.get_psize(), particle.position, { size,size * _renderer->get_aspect_ratio() }, particle.position, { 0,0 }, particle.angle * -57.295779487L);
		}
	}
	_texture.tint = previous_tint;
}

bool BurstParticles::enabled = true;