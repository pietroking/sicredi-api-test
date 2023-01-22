package br.com.sicredi.election.run;


import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages("br.com.sicredi.election.aceitacao")
@IncludeTags("wip")
public class RunTest {
}
