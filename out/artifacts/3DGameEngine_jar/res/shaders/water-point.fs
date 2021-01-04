#version 400 core

#include "water.fsh"
#include "lighting.glh"

uniform PointLight pointLight;

vec4 calcLightEffect(vec3 normal)
{
    return calcSpecularPointLight(pointLight, normal);
}

#include "water-lighting.fsh"

