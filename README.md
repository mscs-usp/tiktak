TIKTAK 0.3
------

Para usar o TikTak 0.3 em seu projeto Maven:

1. Adicionar o repositório Maven do Github da seguinte forma:
```
<repositories>
  <repository>
		<id>tiktak.repo</id>
		<url>https://raw.github.com/tiktak-project/tiktak/mvn-repo/</url>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</snapshots>
  </repository>
</repositories>
```

2. Adicionar a dependência da versão a ser usada da seguinte forma:
```
<dependecies>
  <dependency>
	<groupId>br.org.tiktak</groupId>
	<artifactId>tiktak-api</artifactId>
	<version>0.3</version>
  </dependency>
</dependecies>
```

[Para saber mais sobre o TikTak](https://github.com/tiktak-project/tiktak/wiki/TikTak:-Uma-API-Java-para-feedback-de-usuários)

Licença: LGPL - http://www.gnu.org/copyleft/lesser.html

HISTÓRICO
---------

 * 0.3: Versão estável da API com suporte a sistemas.
 * 0.2: Versão intermediária com suporte parcial a sistemas.
 * 0.1: Versão estável para eventos sem o nome do sistema.
