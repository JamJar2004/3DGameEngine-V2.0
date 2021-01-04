
layout (location = 0) in vec3 in_position;

out vec4 clipSpace;
out vec2 texCoord;
out vec3 worldPosition;
out vec3 toCameraVector;

uniform mat4 modelMatrix;
uniform mat4 MVP;

void main()
{
    vec4 modelPosition = modelMatrix * vec4(in_position, 1.0);

    clipSpace = MVP * vec4(in_position, 1.0);
    gl_Position = MVP * vec4(in_position, 1.0);
    texCoord = (in_position.xz / 2) + 0.5;

    worldPosition = modelPosition.xyz;
}