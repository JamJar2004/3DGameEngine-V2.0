#version 400 core

layout (location = 0) in vec3 in_position;

out vec3 texCoord;

uniform mat4 MVP;

void main()
{
    gl_Position = MVP * vec4(in_position, 1.0);
    texCoord = in_position;
}