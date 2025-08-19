precision mediump float;
varying vec3 Normal;
varying vec3 FragPos;
varying vec2 textureCoord;
  
uniform vec3 lightPos; 
uniform vec3 viewPos; 
uniform vec3 lightColor;
uniform sampler2D objTexture;
uniform float transParent;

void main()
{
      float ambientStrength = 0.1;
      vec3 ambient = ambientStrength * lightColor;
  	
      vec3 norm = normalize(Normal);
      vec3 lightDir = normalize(lightPos - FragPos);
      float diff = max(dot(normalize(norm), normalize(lightDir)), 0.0);
      vec3 diffuse = diff * lightColor;
    
      float specularStrength = 0.5;
      vec3 viewDir = normalize(viewPos - FragPos);
      vec3 reflectDir = normalize(reflect(-lightDir, normalize(norm)));
      float spec = pow(max(dot(normalize(viewDir), normalize(reflectDir)), 0.0), 32.0);
      vec3 specular = specularStrength * spec * lightColor;

      vec3 objectColor = texture2D(objTexture, textureCoord).rgb;
      vec3 result = (ambient + diffuse + specular) * objectColor;
      gl_FragColor = vec4(result, transParent);
} 