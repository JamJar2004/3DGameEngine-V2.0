#version 400 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texCoord;
layout (location = 2) in vec3 in_normal;

out vec3 normal;
out vec3 reflectedVector;
out vec3 refractedVector;

uniform mat4 modelMatrix;
uniform mat4 MVP;

uniform vec3 eyePosition;

uniform float refraction;

void main()
{
    normal = in_normal;
    vec3 worldPosition = (modelMatrix * vec4(in_position, 1.0)).xyz;

    vec3 viewVector = normalize(eyePosition - worldPosition);
    vec3 unitNormal = normalize(in_normal);
    reflectedVector = reflect(viewVector, unitNormal);
    refractedVector = refract(viewVector, unitNormal, 1.0 / (1.0 + refraction));

    gl_Position = MVP * vec4(in_position, 1.0);
}