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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_ML_COMMENT", "RULE_WS", "RULE_NEWLINE", "RULE_INT", "RULE_STRING", "RULE_SL_COMMENT", "RULE_ANY_OTHER", "'{'", "','", "'}'"
    };
    public static final int RULE_ID=4;
    public static final int RULE_WS=6;
    public static final int RULE_NEWLINE=7;
    public static final int RULE_STRING=9;
    public static final int RULE_ANY_OTHER=11;
    public static final int RULE_SL_COMMENT=10;
    public static final int RULE_INT=8;
    public static final int RULE_ML_COMMENT=5;
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
    // InternalTaxanomy.g:71:1: ruleModel returns [EObject current=null] : ( (lv_dimensions_0_0= ruleTerm ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_dimensions_0_0 = null;



        	enterRule();

        try {
            // InternalTaxanomy.g:77:2: ( ( (lv_dimensions_0_0= ruleTerm ) )* )
            // InternalTaxanomy.g:78:2: ( (lv_dimensions_0_0= ruleTerm ) )*
            {
            // InternalTaxanomy.g:78:2: ( (lv_dimensions_0_0= ruleTerm ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalTaxanomy.g:79:3: (lv_dimensions_0_0= ruleTerm )
            	    {
            	    // InternalTaxanomy.g:79:3: (lv_dimensions_0_0= ruleTerm )
            	    // InternalTaxanomy.g:80:4: lv_dimensions_0_0= ruleTerm
            	    {

            	    				newCompositeNode(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_0());
            	    			
            	    pushFollow(FOLLOW_3);
            	    lv_dimensions_0_0=ruleTerm();

            	    state._fsp--;


            	    				if (current==null) {
            	    					current = createModelElementForParent(grammarAccess.getModelRule());
            	    				}
            	    				add(
            	    					current,
            	    					"dimensions",
            	    					lv_dimensions_0_0,
            	    					"de.tudresden.slr.model.Taxanomy.Term");
            	    				afterParserOrEnumRuleCall();
            	    			

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


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
    // InternalTaxanomy.g:100:1: entryRuleTerm returns [EObject current=null] : iv_ruleTerm= ruleTerm EOF ;
    public final EObject entryRuleTerm() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTerm = null;


        try {
            // InternalTaxanomy.g:100:45: (iv_ruleTerm= ruleTerm EOF )
            // InternalTaxanomy.g:101:2: iv_ruleTerm= ruleTerm EOF
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
    // InternalTaxanomy.g:107:1: ruleTerm returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? ) ;
    public final EObject ruleTerm() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_subclasses_2_0 = null;



        	enterRule();

        try {
            // InternalTaxanomy.g:113:2: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? ) )
            // InternalTaxanomy.g:114:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? )
            {
            // InternalTaxanomy.g:114:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? )
            // InternalTaxanomy.g:115:3: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )?
            {
            // InternalTaxanomy.g:115:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalTaxanomy.g:116:4: (lv_name_0_0= RULE_ID )
            {
            // InternalTaxanomy.g:116:4: (lv_name_0_0= RULE_ID )
            // InternalTaxanomy.g:117:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_4); 

            					newLeafNode(lv_name_0_0, grammarAccess.getTermAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTermRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.tudresden.slr.model.Taxanomy.ID");
            				

            }


            }

            // InternalTaxanomy.g:133:3: (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==12) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalTaxanomy.g:134:4: otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}'
                    {
                    otherlv_1=(Token)match(input,12,FOLLOW_5); 

                    				newLeafNode(otherlv_1, grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_1_0());
                    			
                    // InternalTaxanomy.g:138:4: ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0==RULE_ID) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // InternalTaxanomy.g:139:5: ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )?
                    	    {
                    	    // InternalTaxanomy.g:139:5: ( (lv_subclasses_2_0= ruleTerm ) )
                    	    // InternalTaxanomy.g:140:6: (lv_subclasses_2_0= ruleTerm )
                    	    {
                    	    // InternalTaxanomy.g:140:6: (lv_subclasses_2_0= ruleTerm )
                    	    // InternalTaxanomy.g:141:7: lv_subclasses_2_0= ruleTerm
                    	    {

                    	    							newCompositeNode(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_1_1_0_0());
                    	    						
                    	    pushFollow(FOLLOW_5);
                    	    lv_subclasses_2_0=ruleTerm();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getTermRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"subclasses",
                    	    								lv_subclasses_2_0,
                    	    								"de.tudresden.slr.model.Taxanomy.Term");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }

                    	    // InternalTaxanomy.g:158:5: (otherlv_3= ',' )?
                    	    int alt2=2;
                    	    int LA2_0 = input.LA(1);

                    	    if ( (LA2_0==13) ) {
                    	        alt2=1;
                    	    }
                    	    switch (alt2) {
                    	        case 1 :
                    	            // InternalTaxanomy.g:159:6: otherlv_3= ','
                    	            {
                    	            otherlv_3=(Token)match(input,13,FOLLOW_5); 

                    	            						newLeafNode(otherlv_3, grammarAccess.getTermAccess().getCommaKeyword_1_1_1());
                    	            					

                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);

                    otherlv_4=(Token)match(input,14,FOLLOW_2); 

                    				newLeafNode(otherlv_4, grammarAccess.getTermAccess().getRightCurlyBracketKeyword_1_2());
                    			

                    }
                    break;

            }


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

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000006010L});

}