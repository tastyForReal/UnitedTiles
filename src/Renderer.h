#pragma once

#include "Unique.h"
#include "Number.h"

#include <memory>
#include <unordered_set>

class RendererReloadable;
class Texture;

class Renderer : Unique
{
	friend RendererReloadable;
	friend Texture;

public:
	Renderer(bool vsync_ = false);
	~Renderer();

	void clear();
	void display();
	void reload();

	void render(Texture* texture, glm::u32vec2 src_pos, glm::u32vec2 src_size,
		Vec2 dest_pos, Vec2 dest_size,
		Vec2 rot_origin,
		Vec2 src_origin = { 0.0L, 0.0L }, Number angle = 0.0L, uint8_t flip = 0);

	glm::u16vec2 get_size() const;
	Number get_aspect_ratio() const;
	bool active() const;
	bool vsync;

private:
	void* _ptr;
	void* _window;
	std::unordered_set<RendererReloadable*> _reloadables;
	glm::u16vec2 _size;
	Number _aspect_rato;
	bool _widescreen;
	void update_size();
};