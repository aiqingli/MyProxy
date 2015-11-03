package com.hp.ts.dwf.proxy.inject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileInjector implements Injector {

	static String htmlHead = "<link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:400italic,400,700' rel='stylesheet' type='text/css'><link href='http://grommet.io/assets/latest/css/grommet.min.css' rel='stylesheet' type='text/css'><script src=\"https://cdnjs.cloudflare.com/ajax/libs/react/0.13.1/react.js\"></script>  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/react/0.13.1/JSXTransformer.js\"></script><script src=\"http://grommet.io/assets/latest/grommet.min.js\"></script>";
	static String htmlHeader = "  <div id=\"content\"></div>  <script type=\"text/jsx\">    var App = React.createClass({      render: function() {        return (          <Grommet.App>            <Grommet.Header direction=\"row\" justify=\"between\" large={true} pad={{horizontal: 'medium'}}>              <Grommet.Title>Sample</Grommet.Title>            </Grommet.Header>            <Grommet.Section pad={{horizontal: 'medium'}}>";
	static String htmlFooter = "            </Grommet.Section>          </Grommet.App>        );      }    });    var element = document.getElementById('content');    React.render(React.createElement(App), element);  </script>";

	
	
	private File hfDir;
	
	public FileInjector(String realPath) {
		hfDir = new File(new File(realPath), "hf");
	}

	@Override
	public String inject(String html) {
		html = head(html);
		html = header(html);
		html = footer(html);
		return html;
	}

	
	private String readFile(String name) {
		BufferedReader in = null;
		StringBuilder sb = new StringBuilder();
		try {
			in = new BufferedReader(new FileReader(new File(hfDir, name)));
			String line = null;
			while ((line = in.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			
		} catch (IOException e) {
			
		}
		finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
		return sb.toString();
	}
	
	private String footer(String html) {
		return html.replace("</body>", readFile("footer.html") + "</body>");
	}

	private String header(String html) {
		return html.replace("<body>", "<body>" + readFile("header.html"));
	}

	private String head(String html) {
		return html.replace("</head>", readFile("head.html") + "</head>");
	}

	
}
