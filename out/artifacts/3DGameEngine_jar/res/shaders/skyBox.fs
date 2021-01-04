#version 400 core

in vec3 texCoord;

uniform samplerCube cubeMap;
uniform int         cubeMap_EXISTS;

out vec4 outColor;

void main()
{
    vec4 texColor;

    if(cubeMap_EXISTS == 0)
        texColor = vec4(1, 1, 1, 1);
    else
        texColor = texture(cubeMap, texCoord);

    outColor = texColor;
}