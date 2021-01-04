in vec2 texCoord;
in vec3 worldPosition;

uniform float tilingFactor;
uniform float waveStrength;

uniform float moveFactor;

uniform sampler2D distortionMap;
uniform int       distortionMap_EXISTS;

uniform vec3 eyePosition;
