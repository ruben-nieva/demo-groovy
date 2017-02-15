def flow
node{
  echo "bootstrap function begin"
  echo "pwd: " + pwd()
  git url: 'ttps://github.com/ruben-nieva/demo-groovy.git'
  flow = load "05.flow.groovy"
  echo "bootstrap function end"
}
flow.internalBuild()
