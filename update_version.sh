if [ "$1" == '' ]; then
	echo "update_version x.y.z"
	exit 1;
fi

#sed -i -e "s/static private final String version = \"\(.*\)\";/static private final String version = \"$1\";/g" jtransc-rt-core/src/com/jtransc/JTranscVersion.java
sed -i -e "s/jtranscVersion=\(.*\)/jtranscVersion=$1/g" libgdx/cuboc/gradle.properties
sed -i -e "s/jtranscVersion=\(.*\)/jtranscVersion=$1/g" libgdx/invaders/gradle.properties
sed -i -e "s/jtranscVersion=\(.*\)/jtranscVersion=$1/g" libgdx/metagun/gradle.properties
sed -i -e "s/jtranscVersion=\(.*\)/jtranscVersion=$1/g" libgdx/vector-pinball/gradle.properties
sed -i -e "s/jtranscVersion=\(.*\)/jtranscVersion=$1/g" spine-demo/gradle.properties
sed -i -e "s/jtranscVersion=\(.*\)/jtranscVersion=$1/g" hello-world/gradle.properties
mvn versions:set -DnewVersion=$1