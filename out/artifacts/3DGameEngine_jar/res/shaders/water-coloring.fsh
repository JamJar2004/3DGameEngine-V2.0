in vec4 clipSpace;
in vec3 toCameraVector;

uniform float camZNear;
uniform float camZFar;

uniform sampler2D reflection;
uniform int       reflection_EXISTS;

uniform sampler2D refraction;
uniform int       refraction_EXISTS;

uniform sampler2D depthMap;
uniform int       depthMap_EXISTS;

uniform sampler2D normalMap;
uniform int       normalMap_EXISTS;

#include "waterSampling.glh"

out vec4 outColor;

void main()
{
    vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
    vec2 refractTexCoords = ndc;
    vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);

    float waterDepth = calcWaterDepth(depthMap, depthMap_EXISTS, refractTexCoords, camZNear, camZFar, gl_FragCoord.z);

    float finalWaterDepth = clamp(waterDepth / 10.0, 0.0, 1.0);

    float finalWaveStrength = waveStrength;

    if(finalWaveStrength == 0)
        finalWaveStrength = 0.04;

    float finalTilingFactor = tilingFactor;

    if(finalTilingFactor == 0)
        finalTilingFactor = 10;

    vec2 totalDistortion = calcWaterDistortion(distortionMap, texCoord, finalTilingFactor, finalWaveStrength * clamp(waterDepth / 20.0, 0.0, 1.0), moveFactor);

    refractTexCoords += totalDistortion;
    refractTexCoords = clamp(refractTexCoords, 0.001, 0.999);

    reflectTexCoords += totalDistortion;
    reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999);
    reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);

    vec4 reflectionColor = texture2D(reflection, reflectTexCoords);
    vec4 refractionColor = texture2D(refraction, refractTexCoords);

    if(reflection_EXISTS == 0)
        reflectionColor = vec4(0, 0, 1, 1);

    if(refraction_EXISTS == 0)
        reflectionColor = vec4(0, 0, 1, 1);

    vec2 finalTexCoord = texCoord * tilingFactor;
    finalTexCoord += totalDistortion;
    vec3 normal = calcWaterNormals(normalMap, normalMap_EXISTS, finalTexCoord);

    vec3 toCameraVector = normalize(eyePosition - worldPosition);
    vec3 viewVector = normalize(toCameraVector);
    float refractiveFactor = dot(viewVector, normal);

    refractiveFactor = (1 - finalWaterDepth) / 2 + (refractiveFactor / 2);

    outColor = mix(reflectionColor, refractionColor, refractiveFactor);
}