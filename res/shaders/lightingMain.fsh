uniform vec2  tilingFactor;
uniform int   ignoreVertexColor;

#include "sampling.glh"

out vec4 outColor;

void main()
{

    vec2 finalTileFactor = tilingFactor;

    if(finalTileFactor == vec2(0, 0))
        finalTileFactor = vec2(1, 1);

    vec4 normalMapColor = texture2D(normalMap, texCoord * finalTileFactor);
    vec3 normal;

    if(normalMap_EXISTS == 0)
        normal = normalize(tbnMatrix * (2 * vec3(0.5, 0.5, 1) - 1));
    else
        normal = normalize(tbnMatrix * (255.0 / 128.0 * normalMapColor.xyz - 1));

    vec4 totalLight = vec4(0, 0, 0, 1);
    vec4 finalColor = calcColorWithBlendMap(texCoord, finalTileFactor);

    totalLight += calcDiffusedLightEffect(normal);

    vec4 finalVertColor = vec4(vertColor, 1.0);

    if(ignoreVertexColor == 1)
        finalVertColor = vec4(1, 1, 1, 1);

    outColor = finalColor * totalLight * finalVertColor + calcSpecularLightEffect(normal);

    if(outColor.a < 0.5)
        discard;
}