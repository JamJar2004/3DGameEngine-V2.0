#version 400 core

#include "lighting.fsh"
#include "lighting.glh"

uniform DirectionalLight directionalLight;

vec4 calcLightEffect(vec3 normal)
{
    return calcDirectionalLight(directionalLight, normal);
}

vec4 calcDiffusedLightEffect(vec3 normal)
{
    return calcDiffusedDirectionalLight(directionalLight, normal);
}

vec4 calcSpecularLightEffect(vec3 normal)
{
    return calcSpecularDirectionalLight(directionalLight, normal);
}

#include "lightingMain.fsh"