#version 330

in vec2 texCoord;

out vec4 outColor;

uniform sampler2D texture;
uniform int texture_EXISTS;

void main()
{
    vec4 texColor = texture2D(texture, texCoord);

    float maxVal = max(max(texColor.r, texColor.g), texColor.b);
    float a = maxVal;
    float r = texColor.r / maxVal;
    float g = texColor.g / maxVal;
    float b = texColor.b / maxVal;


    texColor = vec4(r, g, b, a);

    if(texture_EXISTS == 0)
        texColor = vec4(0, 0, 0, 1);

    outColor = texColor;
}