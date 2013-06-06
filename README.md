Para usar o TikTak em seu projeto Maven

1. Adicionar o repositorio Maven do Github da seguinte forma:
```
  <repository>
		<id>tiktak.repo</id>
		<url>https://raw.github.com/tiktak-project/tiktak/mvn-repo/</url>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</snapshots>
  </repository>
```

2. Adicionar a dependencia da versão a ser usada da seguinte forma:
```
  <dependency>
	<groupId>br.org.tiktak</groupId>
	<artifactId>tiktak-api</artifactId>
	<version>$VERSAO</version>
  </dependency>
```

Mudando `$VERSAO` para a versão desejada.

[Para saber mais sobre o TikTak](https://github.com/tiktak-project/tiktak/wiki/TikTak:-Uma-API-Java-para-feedback-de-usuários)

Licença: LGPL - http://www.gnu.org/copyleft/lesser.html

HISTORY
-------

 * 0.3: Versão estavel da API com suporte a sistemas
 * 0.2: Versão intermediaria com suporte parcial a sistemas
 * 0.1: Versão estavel para eventos sem o nome do sistema
