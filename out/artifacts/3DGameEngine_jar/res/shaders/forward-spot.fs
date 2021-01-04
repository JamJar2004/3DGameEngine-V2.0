#version 400 core

#include "lighting.fsh"
#include "lighting.glh"

uniform SpotLight spotLight;

vec4 calcLightEffect(vec3 normal)
{
    return calcSpotLight(spotLight, normal);
}

vec4 calcDiffusedLightEffect(vec3 normal)
{
    return calcDiffusedSpotLight(spotLight, normal);
}

vec4 calcSpecularLightEffect(vec3 normal)
{
    return calcSpecularSpotLight(spotLight, normal);
}

#include "lightingMain.fsh"