#version 400 core

in vec3 normal;
in vec3 reflectedVector;
in vec3 refractedVector;

uniform vec3 color;

uniform samplerCube enviroment;
uniform int         enviroment_EXISTS;

uniform float transparency;

out vec4 outColor;

void main()
{
    vec4 reflectionColor = texture(enviroment, reflectedVector);
    vec4 refractionColor = texture(enviroment, refractedVector);

    if (enviroment_EXISTS == 0)
    {
        reflectionColor = vec4(1, 1, 1, 1);
        refractionColor = vec4(1, 1, 1, 1);
    }

    outColor = mix(reflectionColor, refractionColor, transparency) * vec4(color, 1.0);
}