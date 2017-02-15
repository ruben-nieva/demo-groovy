def flow
node{
  echo "bootstrap function begin"
  echo "pwd: " + pwd()
  git url: 'https://github.com/ruben-nieva/demo-groovy.git'
  flow = load "04.flow.groovy"
  echo "bootstrap function end"
}
flow.internalBuild()
