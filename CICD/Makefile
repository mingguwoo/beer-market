# vim: set noet :

.PHONY: clean all test release

MAVEN = mvn
release_path = ./release

clean:
	$(MAVEN) -v
	$(MAVEN) clean
	@rm -rf release
	@rm -rf target

all: clean
	$(MAVEN) -v
	$(MAVEN) package -U -Dmaven.test.skip=true

test: clean
	$(MAVEN) -v
	$(MAVEN) test

release:
	@rm -rf release
	@mkdir -p $(release_path)
	cp -f ./plm-bom-configuration-api/target/plm-bom-configuration-api.jar ${release_path}/plm-bom-configuration.jar
	cp -f CICD/start.sh $(release_path)