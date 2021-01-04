#version 400 core

#include "lighting.fsh"
#include "lighting.glh"

uniform PointLight pointLight;

vec4 calcLightEffect(vec3 normal)
{
    return calcPointLight(pointLight, normal);
}

vec4 calcDiffusedLightEffect(vec3 normal)
{
    return calcDiffusedPointLight(pointLight, normal);
}

vec4 calcSpecularLightEffect(vec3 normal)
{
    return calcSpecularPointLight(pointLight, normal);
}

#include "lightingMain.fsh"