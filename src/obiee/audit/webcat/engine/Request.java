/**
 * 
 */
package obiee.audit.webcat.engine;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents the features entered through the command line. 
 * @author danielgalassi@gmail.com
 *
 */
public class Request {

	private static final Logger logger = LogManager.getLogger(Request.class.getName());

	/** Command line arguments used by the parser application */
	private Options           options = new Options();
	/** Command line representation for Apache Commons CLI */
	private CommandLine           cli = null;

	/**
	 * Constructor
	 * @param args the list of arguments are passed through command line
	 * @throws Exception when command line argument do not result in a feasible execution plan
	 */
	public Request(String[] args) throws Exception {
		createOptions();
		try {
			parseCommandLine(args);
			validatingOptions();
		} catch (Exception e) {
			logger.error("{} thrown while processing command line arguments ({})", e.getClass().getCanonicalName(), e.getMessage());
			throw new Exception("Command line request processing terminated");
		}
	}

	/**
	 * Displays help information
	 */
	private void displayUsage() {
		HelpFormatter help = new HelpFormatter();
		help.printHelp("obiee-security-audit", options);
	}

	/**
	 * Verifies that at least one feature is invoked
	 * @throws Exception when argument dependencies are broken
	 * @see <code>MissingOptionException</code>
	 */
	private void validatingOptions() throws Exception {
		if (cli.hasOption("help")) {
			displayUsage();
		} else 
			if (!cli.hasOption("webcat") || !(cli.hasOption("privs") || cli.hasOption("dashboards"))) {
				throw new MissingOptionException("Insufficient arguments");
			}
	}

	/**
	 * Creates an representation of the command line arguments
	 * @param args command line arguments
	 * @throws Exception when command line arguments are missing or incorrectly entered
	 * @see <code>ParseException</code> 
	 */
	private void parseCommandLine(String[] args) throws Exception {
		if (args.length == 0) {
			logger.fatal("No arguments found");
			throw new ParseException("Invalid request, no arguments found");
		}
		CommandLineParser parser = new GnuParser();
		try {
			cli = parser.parse(options, args);
		} catch (ParseException e) {
			logger.fatal("{} thrown while parsing command line arguments ({})", e.getClass().getCanonicalName(), e.getMessage());
		}
	}

	/**
	 * Sets up all valid command line options. Features can be invoked using a single-letter or verbose option.
	 * 
	 */
	private void createOptions() {
		options.addOption("w", "webcat", true, "the OBIEE web catalogue location");
		options.addOption("p", "privs", false, "invokes a privilege audit (aka who can use these features)");
		options.addOption("d", "dashboards", false, "invokes a dashboard audit (aka who has access to these dashboards/pages)");
		options.addOption("h", "help", false, "displays valid command line arguments");
	}

	public boolean isDashboardAuditInvoked() {
		return cli.hasOption("dashboards");
	}

	public boolean isPrivilegeAuditInvoked() {
		return cli.hasOption("privs");
	}

	public boolean isHelpWanted() {
		return cli.hasOption("help");
	}

	public String getWebcatParam() {
		return cli.getOptionValue("webcat");
	}
}
