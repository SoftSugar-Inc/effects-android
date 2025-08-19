precision mediump float;
attribute vec3 vPosition;
attribute vec3 aNormal;
attribute vec3 vFragPos;
attribute vec2 vTexturePos;

varying vec3 FragPos;
varying vec3 Normal;
varying vec2 textureCoord;

void main()
{
    FragPos = vFragPos;
    Normal = aNormal;
    textureCoord = vTexturePos;

    gl_Position = vec4(vPosition, 1.0);
}