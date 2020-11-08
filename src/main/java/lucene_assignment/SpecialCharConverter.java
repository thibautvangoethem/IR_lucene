package lucene_assignment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


public class SpecialCharConverter {
//	"\\" ,"+", "-" ,"=" ,"&&" ,"||" ,">", "<","!" ,"(" ,")" ,"{", "}" ,"[" ,"]" ,"^" ,"\"", "~" ,"*" ,"?" ,":" ,"/"
	private static List<String> regexMatchList=Arrays.asList("\\\\" ,"\\+", "-" ,"=" ,"&&" ,"\\|\\|" ,">", "<","\\!" ,"[(]" ,"[)]" ,"\\{", "\\}" ,"\\[" ,"\\]" ,"\\^" ,"\"", "~" ,"\\*" ,"\\?" ,":" ,"/");
	private static Map<String, String> specialCharToStringMap  = new HashMap<String, String>() {{
	    put("\\", " backslash");
	    put("+", " plus");
	    put("-", " minus");
	    put("=", " equal");
	    put("||", " logor");
	    put("&&", " logand");//not just 'and' so it isn't removed in the englishfilter of the englishanalyzer
	    put(">", " largerthen");
	    put("<", " smallerthen");
	    put("!", " exclamation");
	    put("(", " leftbracket");
	    put(")", " rigthbracket");
	    put("{", " leftcurly");
	    put("}", " rightcurly");
	    put("[", " leftsquare");
	    put("]", " rightsquare");
	    put("^", " caret");
	    put("\"", " quotation");
	    put("~", " tilda");
	    put("*", " star");
	    put("?", " question");
	    put(":", " doublepoint");
	    put("/", " forwarslash");
	}};
	
	public static String encode(String input) {
		if(input==null)return null;
		String patternString = "(" + StringUtils.join(regexMatchList, "|") + ")";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(input);

		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
		    matcher.appendReplacement(sb, specialCharToStringMap.get(matcher.group(1)));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	
}
