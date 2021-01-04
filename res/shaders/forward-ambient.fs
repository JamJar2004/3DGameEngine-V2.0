#version 400 core

in vec2 texCoord;
in vec3 vertColor;

uniform vec3      ambientLight;
uniform vec2      tilingFactor;
uniform int       ignoreVertexColor;

#include "sampling.glh"

out vec4 outColor;

void main()
{
    vec2 finalTileFactor = tilingFactor;

    if(finalTileFactor == vec2(0, 0))
        finalTileFactor = vec2(1, 1);

    vec4 finalColor = calcColorWithBlendMap(texCoord, finalTileFactor);

    vec4 finalVertColor = vec4(vertColor, 1.0);

    if(ignoreVertexColor == 1)
        finalVertColor = vec4(1, 1, 1, 1);

    outColor = finalColor * vec4(ambientLight, 1.0) * finalVertColor;

    if(outColor.a < 0.5)
        discard;
}