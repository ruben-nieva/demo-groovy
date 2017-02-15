
def internalBuild(){
  echo "internalBuild() function begin"

  stage name: "build", concurrency: 1
  node{
    def mvnHome = tool 'M3.2'

    sh "${mvnHome}/bin/mvn clean"
    sh "${mvnHome}/bin/mvn pl.project13.maven:git-commit-id-plugin:revision"

    def strVersion       = getVersionStringFromPom()
    def majorVersion     = getMajorVersion( strVersion )
    def minorVersion     = getMinorVersion( strVersion )
    def strGitProperties = readFile( 'git.properties' )
    def commitTimestamp  = getCommitTimestamp( strGitProperties )

    echo "strVersion     : $strVersion"
    echo "majorVersion   : $majorVersion"
    echo "minorVersion   : $minorVersion"
    echo "commitTimestamp: $commitTimestamp"

    sh "${mvnHome}/bin/mvn versions:set -DnewVersion=${majorVersion}.${minorVersion}-${commitTimestamp}"

    echo "NEXUS_USER    : ${env.NEXUS_USER}"
    echo "NEXUS_USER_PWD: ${env.NEXUS_USER_PWD}"

    sh 'echo NEXUS_USER    : $NEXUS_USER'
    sh 'echo NEXUS_USER_PWD: $NEXUS_USER_PWD'

    def nexusUrl = getUrl( env.NEXUS_PORT )

    echo "ENV.NEXUS_URL: ${env.NEXUS_PORT}"
    echo "NEXUS_URL    : ${nexusUrl}"

    sh "${mvnHome}/bin/mvn deploy -DaltDeploymentRepository=nexus::default::http://${nexusUrl}/content/repositories/releases -U"
  }

  echo "internalBuild() function end"
}

return this;

// ==========================================================================================================

def String getMajorVersion( String str )
{
  def matcher = str =~ '(\\d+)[.-](\\d+)[.-](.*)'
  matcher ? matcher[0][1] : null
}

def String getMinorVersion( String str )
{
  def matcher = str =~ '(\\d+)[.-](\\d+)[.-](.*)'
  matcher ? matcher[0][2] : null
}

def String getPatchVersion( String str )
{
  def matcher = str =~ '(\\d+)[.-](\\d+)[.-](.*)'
  matcher ? matcher[0][3] : null
}

def getVersionStringFromPom()
{
  def matcher = readFile( 'pom.xml' ) =~ '<version>(.+)</version>'
  matcher ? matcher[0][1] : null
}

def String getCommitTimestamp( String str )
{
  def matcher = str =~ 'git.commit.time=(.+)'
  matcher ? matcher[0][1] : null
}

def String getUrl( String str )
{
  def matcher = str =~ '.*://(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{1,5}).*'
  matcher ? matcher[0][1] : null
}
