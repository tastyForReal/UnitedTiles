#pragma once

#include "RendererReloadable.h"
#include "Types.h"
#include "Colors.h"

#include <string>
#include <variant>

class Renderer;
class Font;

class Texture : RendererReloadable
{
	friend Renderer;

public:
	Texture(Renderer* renderer, const std::string& filename);
	Texture(Renderer* renderer, Font* font, const std::string& text, Color color);
	~Texture();

	enum class Type : uint8_t {
		FILE, FONT
	} const type;
	struct FileInfo {
		const std::string filename;
	};
	struct FontInfo {
		const std::string filename;
		const Number size;
		const std::string text;
		const Color color;
	};
	const std::variant<FileInfo, FontInfo> info;

	Color tint = Colors::WHITE;
	uint8_t blend_mode;

	glm::u32vec2 get_psize() const;
	Vec2 get_rsize() const;

private:
	void* _ptr;
	glm::u32vec2 _psize;
	Vec2 _rsize;
	void update_size();
	void unload() override;
	void reload() override;
};