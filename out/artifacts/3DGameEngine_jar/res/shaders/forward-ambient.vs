#version 400 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texCoord;

out vec2 texCoord;

uniform mat4 modelMatrix;
uniform mat4 MVP;
uniform vec4 clippingPlane;

void main()
{
    vec4 worldPosition = modelMatrix * vec4(in_position, 1.0);

    gl_ClipDistance[0] = dot(worldPosition, clippingPlane);

    gl_Position = MVP * vec4(in_position, 1.0);
    texCoord = in_texCoord;
}