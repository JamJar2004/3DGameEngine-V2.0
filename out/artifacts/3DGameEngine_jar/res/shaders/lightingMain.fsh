uniform float tilingFactor;

#include "sampling.glh"

out vec4 outColor;

void main()
{

    float finalTileFactor = tilingFactor;

    if(finalTileFactor == 0)
        finalTileFactor = 1;

    vec4 normalMapColor = texture2D(normalMap, texCoord * finalTileFactor);
    vec3 normal;

    if(normalMap_EXISTS == 0)
        normal = normalize(tbnMatrix * (2 * vec3(0.5, 0.5, 1) - 1));
    else
        normal = normalize(tbnMatrix * (255.0 / 128.0 * normalMapColor.xyz - 1));


    vec4 totalLight = vec4(0, 0, 0, 1);
    vec4 finalColor = calcColorWithBlendMap(texCoord, finalTileFactor);

    totalLight += calcDiffusedLightEffect(normal);

    outColor = finalColor * totalLight + calcSpecularLightEffect(normal);

    if(outColor.a < 0.5)
        discard;
}