#version 400 core

#include "water.fsh"
#include "lighting.glh"

uniform SpotLight spotLight;

vec4 calcLightEffect(vec3 normal)
{
    return calcSpecularSpotLight(spotLight, normal);
}

#include "water-lighting.fsh"