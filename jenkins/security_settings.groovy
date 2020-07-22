// /var/lib/jenkins/init.groovy.d/security_settings.groovy
//
//
import jenkins.model.*
import jenkins.AgentProtocol
import hudson.security.csrf.DefaultCrumbIssuer
import jenkins.security.s2m.AdminWhitelistRule
import hudson.model.PageDecorator

def instance = Jenkins.getInstance()

// Disable deprecated Jenkins node protocols
p = AgentProtocol.all()

disable_plugin_list = ["JNLP-connect", "JNLP2-connect", "JNLP3-connect", "CLI-connect", "CLI2-connect"]

p.each { x ->
	if(x.name && x.name in disable_plugin_list ) {
      p.remove(x)
    }
}

// Enable CSRF protection
instance.setCrumbIssuer(new DefaultCrumbIssuer(true))

// Enable agent to master security subsystem
Jenkins.instance.getInjector().getInstance(AdminWhitelistRule.class)
.setMasterKillSwitch(false)

// Disable Jenkins CLI
jenkins.CLI.get().setEnabled(false)

// Set a nicer theme while we're at it
for (pd in PageDecorator.all()) {
  if (pd instanceof org.codefirst.SimpleThemeDecorator) {
    pd.cssUrl = 'https://jenkins-contrib-themes.github.io/jenkins-material-theme/dist/material-blue-grey.css'
  }
}

// Set matrix-based user security and allow authenticated
def strategy = new hudson.security.GlobalMatrixAuthorizationStrategy()
strategy.add(Jenkins.ADMINISTER,'authenticated')
instance.setAuthorizationStrategy(strategy)

instance.save()
