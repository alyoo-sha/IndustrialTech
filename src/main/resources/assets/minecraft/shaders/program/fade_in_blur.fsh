#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 BlurDir;
uniform float Radius;
uniform float Progress;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

void main() {
    float rad = Radius * Progress;
    int n = int(floor(rad));
    float frac = rad - float(n);
    vec2 step = oneTexel * BlurDir;

    vec3 sum = texture(DiffuseSampler, texCoord).rgb;
    for (int i = 1; i <= n; i++) {
        sum += texture(DiffuseSampler, texCoord + step * float(i)).rgb;
        sum += texture(DiffuseSampler, texCoord - step * float(i)).rgb;
    }
    // дробная часть радиуса: крайняя пара выборок с частичным весом
    float k = float(n + 1);
    sum += frac * (texture(DiffuseSampler, texCoord + step * k).rgb
    + texture(DiffuseSampler, texCoord - step * k).rgb);

    fragColor = vec4(sum / (2.0 * rad + 1.0), 1.0);
}