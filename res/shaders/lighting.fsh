in vec2 texCoord;
in vec3 vertColor;
in vec3 worldPosition;
in mat3 tbnMatrix;

uniform vec3      eyePosition;
uniform sampler2D normalMap;
uniform int       normalMap_EXISTS;