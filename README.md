# jsoup2bean
wrap jsoup to extract java bean from HTML

jsoup is a Java library for working with real-world HTML.It provides a very convenient API 
for extracting and manipulating data, using the best of DOM, CSS, and jquery-like methods.

On working with jsoup, i found 2 shortcomings:
1)to extract infos from a HTML, i should write lots of repeated jsoup codes which are boring
2)the real-word HTML is changed so frequently that i have to waste lots of time on fixing and testing the programme
when it is changed

To make the job efficient, i wrap the jsoup to convert a HTML page into java beans automaticly
by a configuration file. And i name it jsoup2bean.

# Required
JDK 6+
jsoup 1.7.3+
Apache commons
Spring 3+
digester 3+

![image](https://github.com/CarrowZhu/jsoup2bean/componentDiagram.png)

#logging
slf4j

#examples 
See the test cases in the test folders for detail.
