package de.tudresden.slr.model.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.tudresden.slr.model.services.TaxanomyGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTaxanomyParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_ID", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'Model'", "'{'", "'dimensions'", "','", "'}'", "'Term'", "'subclasses'"
    };
    public static final int RULE_ID=5;
    public static final int RULE_WS=9;
    public static final int RULE_STRING=4;
    public static final int RULE_ANY_OTHER=10;
    public static final int RULE_SL_COMMENT=8;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int RULE_INT=6;
    public static final int T__11=11;
    public static final int RULE_ML_COMMENT=7;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int EOF=-1;

    // delegates
    // delegators


        public InternalTaxanomyParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalTaxanomyParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalTaxanomyParser.tokenNames; }
    public String getGrammarFileName() { return "InternalTaxanomy.g"; }



     	private TaxanomyGrammarAccess grammarAccess;

        public InternalTaxanomyParser(TokenStream input, TaxanomyGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "Model";
       	}

       	@Override
       	protected TaxanomyGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleModel"
    // InternalTaxanomy.g:64:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalTaxanomy.g:64:46: (iv_ruleModel= ruleModel EOF )
            // InternalTaxanomy.g:65:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalTaxanomy.g:71:1: ruleModel returns [EObject current=null] : ( () otherlv_1= 'Model' otherlv_2= '{' (otherlv_3= 'dimensions' otherlv_4= '{' ( (lv_dimensions_5_0= ruleTerm ) ) (otherlv_6= ',' ( (lv_dimensions_7_0= ruleTerm ) ) )* otherlv_8= '}' )? otherlv_9= '}' ) ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        EObject lv_dimensions_5_0 = null;

        EObject lv_dimensions_7_0 = null;



        	enterRule();

        try {
            // InternalTaxanomy.g:77:2: ( ( () otherlv_1= 'Model' otherlv_2= '{' (otherlv_3= 'dimensions' otherlv_4= '{' ( (lv_dimensions_5_0= ruleTerm ) ) (otherlv_6= ',' ( (lv_dimensions_7_0= ruleTerm ) ) )* otherlv_8= '}' )? otherlv_9= '}' ) )
            // InternalTaxanomy.g:78:2: ( () otherlv_1= 'Model' otherlv_2= '{' (otherlv_3= 'dimensions' otherlv_4= '{' ( (lv_dimensions_5_0= ruleTerm ) ) (otherlv_6= ',' ( (lv_dimensions_7_0= ruleTerm ) ) )* otherlv_8= '}' )? otherlv_9= '}' )
            {
            // InternalTaxanomy.g:78:2: ( () otherlv_1= 'Model' otherlv_2= '{' (otherlv_3= 'dimensions' otherlv_4= '{' ( (lv_dimensions_5_0= ruleTerm ) ) (otherlv_6= ',' ( (lv_dimensions_7_0= ruleTerm ) ) )* otherlv_8= '}' )? otherlv_9= '}' )
            // InternalTaxanomy.g:79:3: () otherlv_1= 'Model' otherlv_2= '{' (otherlv_3= 'dimensions' otherlv_4= '{' ( (lv_dimensions_5_0= ruleTerm ) ) (otherlv_6= ',' ( (lv_dimensions_7_0= ruleTerm ) ) )* otherlv_8= '}' )? otherlv_9= '}'
            {
            // InternalTaxanomy.g:79:3: ()
            // InternalTaxanomy.g:80:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getModelAccess().getModelAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,11,FOLLOW_3); 

            			newLeafNode(otherlv_1, grammarAccess.getModelAccess().getModelKeyword_1());
            		
            otherlv_2=(Token)match(input,12,FOLLOW_4); 

            			newLeafNode(otherlv_2, grammarAccess.getModelAccess().getLeftCurlyBracketKeyword_2());
            		
            // InternalTaxanomy.g:94:3: (otherlv_3= 'dimensions' otherlv_4= '{' ( (lv_dimensions_5_0= ruleTerm ) ) (otherlv_6= ',' ( (lv_dimensions_7_0= ruleTerm ) ) )* otherlv_8= '}' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==13) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // InternalTaxanomy.g:95:4: otherlv_3= 'dimensions' otherlv_4= '{' ( (lv_dimensions_5_0= ruleTerm ) ) (otherlv_6= ',' ( (lv_dimensions_7_0= ruleTerm ) ) )* otherlv_8= '}'
                    {
                    otherlv_3=(Token)match(input,13,FOLLOW_3); 

                    				newLeafNode(otherlv_3, grammarAccess.getModelAccess().getDimensionsKeyword_3_0());
                    			
                    otherlv_4=(Token)match(input,12,FOLLOW_5); 

                    				newLeafNode(otherlv_4, grammarAccess.getModelAccess().getLeftCurlyBracketKeyword_3_1());
                    			
                    // InternalTaxanomy.g:103:4: ( (lv_dimensions_5_0= ruleTerm ) )
                    // InternalTaxanomy.g:104:5: (lv_dimensions_5_0= ruleTerm )
                    {
                    // InternalTaxanomy.g:104:5: (lv_dimensions_5_0= ruleTerm )
                    // InternalTaxanomy.g:105:6: lv_dimensions_5_0= ruleTerm
                    {

                    						newCompositeNode(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_3_2_0());
                    					
                    pushFollow(FOLLOW_6);
                    lv_dimensions_5_0=ruleTerm();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getModelRule());
                    						}
                    						add(
                    							current,
                    							"dimensions",
                    							lv_dimensions_5_0,
                    							"de.tudresden.slr.model.Taxanomy.Term");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalTaxanomy.g:122:4: (otherlv_6= ',' ( (lv_dimensions_7_0= ruleTerm ) ) )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( (LA1_0==14) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // InternalTaxanomy.g:123:5: otherlv_6= ',' ( (lv_dimensions_7_0= ruleTerm ) )
                    	    {
                    	    otherlv_6=(Token)match(input,14,FOLLOW_5); 

                    	    					newLeafNode(otherlv_6, grammarAccess.getModelAccess().getCommaKeyword_3_3_0());
                    	    				
                    	    // InternalTaxanomy.g:127:5: ( (lv_dimensions_7_0= ruleTerm ) )
                    	    // InternalTaxanomy.g:128:6: (lv_dimensions_7_0= ruleTerm )
                    	    {
                    	    // InternalTaxanomy.g:128:6: (lv_dimensions_7_0= ruleTerm )
                    	    // InternalTaxanomy.g:129:7: lv_dimensions_7_0= ruleTerm
                    	    {

                    	    							newCompositeNode(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_3_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_6);
                    	    lv_dimensions_7_0=ruleTerm();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getModelRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"dimensions",
                    	    								lv_dimensions_7_0,
                    	    								"de.tudresden.slr.model.Taxanomy.Term");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop1;
                        }
                    } while (true);

                    otherlv_8=(Token)match(input,15,FOLLOW_7); 

                    				newLeafNode(otherlv_8, grammarAccess.getModelAccess().getRightCurlyBracketKeyword_3_4());
                    			

                    }
                    break;

            }

            otherlv_9=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_9, grammarAccess.getModelAccess().getRightCurlyBracketKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleTerm"
    // InternalTaxanomy.g:160:1: entryRuleTerm returns [EObject current=null] : iv_ruleTerm= ruleTerm EOF ;
    public final EObject entryRuleTerm() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTerm = null;


        try {
            // InternalTaxanomy.g:160:45: (iv_ruleTerm= ruleTerm EOF )
            // InternalTaxanomy.g:161:2: iv_ruleTerm= ruleTerm EOF
            {
             newCompositeNode(grammarAccess.getTermRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTerm=ruleTerm();

            state._fsp--;

             current =iv_ruleTerm; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTerm"


    // $ANTLR start "ruleTerm"
    // InternalTaxanomy.g:167:1: ruleTerm returns [EObject current=null] : ( () otherlv_1= 'Term' ( (lv_name_2_0= ruleEString ) ) otherlv_3= '{' (otherlv_4= 'subclasses' otherlv_5= '{' ( (lv_subclasses_6_0= ruleTerm ) ) (otherlv_7= ',' ( (lv_subclasses_8_0= ruleTerm ) ) )* otherlv_9= '}' )? otherlv_10= '}' ) ;
    public final EObject ruleTerm() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_10=null;
        AntlrDatatypeRuleToken lv_name_2_0 = null;

        EObject lv_subclasses_6_0 = null;

        EObject lv_subclasses_8_0 = null;



        	enterRule();

        try {
            // InternalTaxanomy.g:173:2: ( ( () otherlv_1= 'Term' ( (lv_name_2_0= ruleEString ) ) otherlv_3= '{' (otherlv_4= 'subclasses' otherlv_5= '{' ( (lv_subclasses_6_0= ruleTerm ) ) (otherlv_7= ',' ( (lv_subclasses_8_0= ruleTerm ) ) )* otherlv_9= '}' )? otherlv_10= '}' ) )
            // InternalTaxanomy.g:174:2: ( () otherlv_1= 'Term' ( (lv_name_2_0= ruleEString ) ) otherlv_3= '{' (otherlv_4= 'subclasses' otherlv_5= '{' ( (lv_subclasses_6_0= ruleTerm ) ) (otherlv_7= ',' ( (lv_subclasses_8_0= ruleTerm ) ) )* otherlv_9= '}' )? otherlv_10= '}' )
            {
            // InternalTaxanomy.g:174:2: ( () otherlv_1= 'Term' ( (lv_name_2_0= ruleEString ) ) otherlv_3= '{' (otherlv_4= 'subclasses' otherlv_5= '{' ( (lv_subclasses_6_0= ruleTerm ) ) (otherlv_7= ',' ( (lv_subclasses_8_0= ruleTerm ) ) )* otherlv_9= '}' )? otherlv_10= '}' )
            // InternalTaxanomy.g:175:3: () otherlv_1= 'Term' ( (lv_name_2_0= ruleEString ) ) otherlv_3= '{' (otherlv_4= 'subclasses' otherlv_5= '{' ( (lv_subclasses_6_0= ruleTerm ) ) (otherlv_7= ',' ( (lv_subclasses_8_0= ruleTerm ) ) )* otherlv_9= '}' )? otherlv_10= '}'
            {
            // InternalTaxanomy.g:175:3: ()
            // InternalTaxanomy.g:176:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getTermAccess().getTermAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,16,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getTermAccess().getTermKeyword_1());
            		
            // InternalTaxanomy.g:186:3: ( (lv_name_2_0= ruleEString ) )
            // InternalTaxanomy.g:187:4: (lv_name_2_0= ruleEString )
            {
            // InternalTaxanomy.g:187:4: (lv_name_2_0= ruleEString )
            // InternalTaxanomy.g:188:5: lv_name_2_0= ruleEString
            {

            					newCompositeNode(grammarAccess.getTermAccess().getNameEStringParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_3);
            lv_name_2_0=ruleEString();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTermRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_2_0,
            						"de.tudresden.slr.model.Taxanomy.EString");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,12,FOLLOW_9); 

            			newLeafNode(otherlv_3, grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalTaxanomy.g:209:3: (otherlv_4= 'subclasses' otherlv_5= '{' ( (lv_subclasses_6_0= ruleTerm ) ) (otherlv_7= ',' ( (lv_subclasses_8_0= ruleTerm ) ) )* otherlv_9= '}' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==17) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalTaxanomy.g:210:4: otherlv_4= 'subclasses' otherlv_5= '{' ( (lv_subclasses_6_0= ruleTerm ) ) (otherlv_7= ',' ( (lv_subclasses_8_0= ruleTerm ) ) )* otherlv_9= '}'
                    {
                    otherlv_4=(Token)match(input,17,FOLLOW_3); 

                    				newLeafNode(otherlv_4, grammarAccess.getTermAccess().getSubclassesKeyword_4_0());
                    			
                    otherlv_5=(Token)match(input,12,FOLLOW_5); 

                    				newLeafNode(otherlv_5, grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_4_1());
                    			
                    // InternalTaxanomy.g:218:4: ( (lv_subclasses_6_0= ruleTerm ) )
                    // InternalTaxanomy.g:219:5: (lv_subclasses_6_0= ruleTerm )
                    {
                    // InternalTaxanomy.g:219:5: (lv_subclasses_6_0= ruleTerm )
                    // InternalTaxanomy.g:220:6: lv_subclasses_6_0= ruleTerm
                    {

                    						newCompositeNode(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_4_2_0());
                    					
                    pushFollow(FOLLOW_6);
                    lv_subclasses_6_0=ruleTerm();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getTermRule());
                    						}
                    						add(
                    							current,
                    							"subclasses",
                    							lv_subclasses_6_0,
                    							"de.tudresden.slr.model.Taxanomy.Term");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalTaxanomy.g:237:4: (otherlv_7= ',' ( (lv_subclasses_8_0= ruleTerm ) ) )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0==14) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // InternalTaxanomy.g:238:5: otherlv_7= ',' ( (lv_subclasses_8_0= ruleTerm ) )
                    	    {
                    	    otherlv_7=(Token)match(input,14,FOLLOW_5); 

                    	    					newLeafNode(otherlv_7, grammarAccess.getTermAccess().getCommaKeyword_4_3_0());
                    	    				
                    	    // InternalTaxanomy.g:242:5: ( (lv_subclasses_8_0= ruleTerm ) )
                    	    // InternalTaxanomy.g:243:6: (lv_subclasses_8_0= ruleTerm )
                    	    {
                    	    // InternalTaxanomy.g:243:6: (lv_subclasses_8_0= ruleTerm )
                    	    // InternalTaxanomy.g:244:7: lv_subclasses_8_0= ruleTerm
                    	    {

                    	    							newCompositeNode(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_4_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_6);
                    	    lv_subclasses_8_0=ruleTerm();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getTermRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"subclasses",
                    	    								lv_subclasses_8_0,
                    	    								"de.tudresden.slr.model.Taxanomy.Term");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);

                    otherlv_9=(Token)match(input,15,FOLLOW_7); 

                    				newLeafNode(otherlv_9, grammarAccess.getTermAccess().getRightCurlyBracketKeyword_4_4());
                    			

                    }
                    break;

            }

            otherlv_10=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_10, grammarAccess.getTermAccess().getRightCurlyBracketKeyword_5());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTerm"


    // $ANTLR start "entryRuleEString"
    // InternalTaxanomy.g:275:1: entryRuleEString returns [String current=null] : iv_ruleEString= ruleEString EOF ;
    public final String entryRuleEString() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleEString = null;


        try {
            // InternalTaxanomy.g:275:47: (iv_ruleEString= ruleEString EOF )
            // InternalTaxanomy.g:276:2: iv_ruleEString= ruleEString EOF
            {
             newCompositeNode(grammarAccess.getEStringRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEString=ruleEString();

            state._fsp--;

             current =iv_ruleEString.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEString"


    // $ANTLR start "ruleEString"
    // InternalTaxanomy.g:282:1: ruleEString returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID ) ;
    public final AntlrDatatypeRuleToken ruleEString() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_STRING_0=null;
        Token this_ID_1=null;


        	enterRule();

        try {
            // InternalTaxanomy.g:288:2: ( (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID ) )
            // InternalTaxanomy.g:289:2: (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID )
            {
            // InternalTaxanomy.g:289:2: (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==RULE_STRING) ) {
                alt5=1;
            }
            else if ( (LA5_0==RULE_ID) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalTaxanomy.g:290:3: this_STRING_0= RULE_STRING
                    {
                    this_STRING_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    			current.merge(this_STRING_0);
                    		

                    			newLeafNode(this_STRING_0, grammarAccess.getEStringAccess().getSTRINGTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalTaxanomy.g:298:3: this_ID_1= RULE_ID
                    {
                    this_ID_1=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_1);
                    		

                    			newLeafNode(this_ID_1, grammarAccess.getEStringAccess().getIDTerminalRuleCall_1());
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEString"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x000000000000A000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000028000L});

}