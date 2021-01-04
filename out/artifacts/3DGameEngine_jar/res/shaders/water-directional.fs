#version 400 core

#include "water.fsh"
#include "lighting.glh"

uniform DirectionalLight directionalLight;

vec4 calcLightEffect(vec3 normal)
{
    return calcSpecularDirectionalLight(directionalLight, normal);
}

#include "water-lighting.fsh"

