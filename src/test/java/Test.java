import org.apache.commons.lang.StringUtils;

/**
 * Created by hellboy on 2015/10/6.
 */
public class Test {
    public static void main(String[] args){
        String value = "[\"9\",[{opendate:\"2015-10-09\",ticktime:\"15:00:00\",trade:\"14.9600\",changeratio:\"-0.0209424\",inamount:\"423225617.4400\",outamount:\"439783633.7800\",netamount:\"-16558016.3400\",ratioamount:\"-0.018505\",r0_ratio:\"-0.0483029\",r3_ratio:\"0.00152767\"}]]";
        int start = value.indexOf(",");
        int end = value.lastIndexOf("]");

        String result = value.substring(start+1,end);
        System.out.print(result);
    }
}
