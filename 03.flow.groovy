
def internalBuild(){
  echo "internalBuild() function begin"

  stage name: "build", concurrency: 1
  node{
    echo "Hallo from flow.groovy"
  }

  echo "internalBuild() function end"
}

return this;
