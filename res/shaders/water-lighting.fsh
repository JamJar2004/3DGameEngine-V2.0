uniform sampler2D depthMap;
uniform int       depthMap_EXISTS;

uniform sampler2D normalMap;
uniform int       normalMap_EXISTS;

uniform float camZNear;
uniform float camZFar;

#include "waterSampling.glh"

out vec4 outColor;

void main()
{
    float finalWaveStrength = waveStrength;

    if(finalWaveStrength == 0)
        finalWaveStrength = 0.04;

    float finalTilingFactor = tilingFactor;

    if(finalTilingFactor == 0)
        finalTilingFactor = 10;

    vec2 totalDistortion = calcWaterDistortion(distortionMap, texCoord, finalTilingFactor, finalWaveStrength, moveFactor);

    vec2 finalTexCoord = finalTilingFactor * texCoord;

    float waterDepth = calcWaterDepth(depthMap, depthMap_EXISTS, texCoord, camZNear, camZFar, gl_FragCoord.z);

    vec2 finalDistortion = finalTexCoord + totalDistortion;
    vec3 normal = calcWaterNormals(normalMap, normalMap_EXISTS, finalDistortion);

    vec4 specularColor = calcLightEffect(normal);

    outColor = specularColor;
}