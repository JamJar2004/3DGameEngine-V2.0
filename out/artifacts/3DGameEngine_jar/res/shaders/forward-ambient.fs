#version 400 core

in vec2 texCoord;

uniform vec3      ambientLight;
uniform float     tilingFactor;

#include "sampling.glh"

out vec4 outColor;

void main()
{
    float finalTileFactor = tilingFactor;

    if(finalTileFactor == 0)
        finalTileFactor = 1;

    vec4 finalColor = calcColorWithBlendMap(texCoord, finalTileFactor);

    outColor = finalColor * vec4(ambientLight, 1.0);

    if(outColor.a < 0.5)
        discard;
}