layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texCoord;
layout (location = 2) in vec3 in_normal;
layout (location = 3) in vec3 in_tangent;

out vec2 texCoord;
out vec3 worldPosition;
out mat3 tbnMatrix;

uniform mat4 modelMatrix;
uniform mat4 MVP;

uniform vec4 clippingPlane;

void main()
{
    vec4 worldPosition2 = modelMatrix * vec4(in_position, 1.0);

    gl_ClipDistance[0] = dot(worldPosition2, clippingPlane);

    gl_Position    = MVP * vec4(in_position, 1.0);
    texCoord       = in_texCoord;

    worldPosition = (modelMatrix * vec4(in_position, 1.0)).xyz;

    vec3 n = normalize((modelMatrix * vec4(in_normal, 0.0)).xyz);
    vec3 t = normalize((modelMatrix * vec4(in_tangent, 0.0)).xyz);
    t = normalize(t - dot(t, n) * n);

    vec3 biTangent = cross(t, n);
    tbnMatrix = mat3(t, biTangent, n);
}