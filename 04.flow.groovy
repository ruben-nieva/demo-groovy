
def internalBuild(){
  echo "internalBuild() function begin"

  stage name: "build", concurrency: 1
  node{
    def mvnHome = tool 'M3'
    sh "${mvnHome}/bin/mvn clean package"
  }

  echo "internalBuild() function end"
}

return this;
