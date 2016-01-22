package de.tudresden.slr.model.taxonomy.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.tudresden.slr.model.taxonomy.services.TaxonomyGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTaxonomyParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_SL_COMMENT", "RULE_ML_COMMENT", "RULE_WS", "RULE_NEWLINE", "'{'", "','", "'}'"
    };
    public static final int T__9=9;
    public static final int RULE_ID=4;
    public static final int RULE_WS=7;
    public static final int RULE_NEWLINE=8;
    public static final int RULE_SL_COMMENT=5;
    public static final int T__11=11;
    public static final int RULE_ML_COMMENT=6;
    public static final int EOF=-1;
    public static final int T__10=10;

    // delegates
    // delegators


        public InternalTaxonomyParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalTaxonomyParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalTaxonomyParser.tokenNames; }
    public String getGrammarFileName() { return "InternalTaxonomy.g"; }



     	private TaxonomyGrammarAccess grammarAccess;
     	
        public InternalTaxonomyParser(TokenStream input, TaxonomyGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "Model";	
       	}
       	
       	@Override
       	protected TaxonomyGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start "entryRuleModel"
    // InternalTaxonomy.g:67:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalTaxonomy.g:68:2: (iv_ruleModel= ruleModel EOF )
            // InternalTaxonomy.g:69:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FollowSets000.FOLLOW_1);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FollowSets000.FOLLOW_2); 

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
    // InternalTaxonomy.g:76:1: ruleModel returns [EObject current=null] : ( (lv_dimensions_0_0= ruleTerm ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_dimensions_0_0 = null;


         enterRule(); 
            
        try {
            // InternalTaxonomy.g:79:28: ( ( (lv_dimensions_0_0= ruleTerm ) )* )
            // InternalTaxonomy.g:80:1: ( (lv_dimensions_0_0= ruleTerm ) )*
            {
            // InternalTaxonomy.g:80:1: ( (lv_dimensions_0_0= ruleTerm ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalTaxonomy.g:81:1: (lv_dimensions_0_0= ruleTerm )
            	    {
            	    // InternalTaxonomy.g:81:1: (lv_dimensions_0_0= ruleTerm )
            	    // InternalTaxonomy.g:82:3: lv_dimensions_0_0= ruleTerm
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_0()); 
            	    	    
            	    pushFollow(FollowSets000.FOLLOW_3);
            	    lv_dimensions_0_0=ruleTerm();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getModelRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"dimensions",
            	            		lv_dimensions_0_0, 
            	            		"de.tudresden.slr.model.taxonomy.Taxonomy.Term");
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
    // InternalTaxonomy.g:106:1: entryRuleTerm returns [EObject current=null] : iv_ruleTerm= ruleTerm EOF ;
    public final EObject entryRuleTerm() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTerm = null;


        try {
            // InternalTaxonomy.g:107:2: (iv_ruleTerm= ruleTerm EOF )
            // InternalTaxonomy.g:108:2: iv_ruleTerm= ruleTerm EOF
            {
             newCompositeNode(grammarAccess.getTermRule()); 
            pushFollow(FollowSets000.FOLLOW_1);
            iv_ruleTerm=ruleTerm();

            state._fsp--;

             current =iv_ruleTerm; 
            match(input,EOF,FollowSets000.FOLLOW_2); 

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
    // InternalTaxonomy.g:115:1: ruleTerm returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? ) ;
    public final EObject ruleTerm() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_subclasses_2_0 = null;


         enterRule(); 
            
        try {
            // InternalTaxonomy.g:118:28: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? ) )
            // InternalTaxonomy.g:119:1: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? )
            {
            // InternalTaxonomy.g:119:1: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? )
            // InternalTaxonomy.g:119:2: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )?
            {
            // InternalTaxonomy.g:119:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalTaxonomy.g:120:1: (lv_name_0_0= RULE_ID )
            {
            // InternalTaxonomy.g:120:1: (lv_name_0_0= RULE_ID )
            // InternalTaxonomy.g:121:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FollowSets000.FOLLOW_4); 

            			newLeafNode(lv_name_0_0, grammarAccess.getTermAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getTermRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"de.tudresden.slr.model.taxonomy.Taxonomy.ID");
            	    

            }


            }

            // InternalTaxonomy.g:137:2: (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==9) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalTaxonomy.g:137:4: otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}'
                    {
                    otherlv_1=(Token)match(input,9,FollowSets000.FOLLOW_5); 

                        	newLeafNode(otherlv_1, grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_1_0());
                        
                    // InternalTaxonomy.g:141:1: ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0==RULE_ID) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // InternalTaxonomy.g:141:2: ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )?
                    	    {
                    	    // InternalTaxonomy.g:141:2: ( (lv_subclasses_2_0= ruleTerm ) )
                    	    // InternalTaxonomy.g:142:1: (lv_subclasses_2_0= ruleTerm )
                    	    {
                    	    // InternalTaxonomy.g:142:1: (lv_subclasses_2_0= ruleTerm )
                    	    // InternalTaxonomy.g:143:3: lv_subclasses_2_0= ruleTerm
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_1_1_0_0()); 
                    	    	    
                    	    pushFollow(FollowSets000.FOLLOW_5);
                    	    lv_subclasses_2_0=ruleTerm();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getTermRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"subclasses",
                    	            		lv_subclasses_2_0, 
                    	            		"de.tudresden.slr.model.taxonomy.Taxonomy.Term");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }

                    	    // InternalTaxonomy.g:159:2: (otherlv_3= ',' )?
                    	    int alt2=2;
                    	    int LA2_0 = input.LA(1);

                    	    if ( (LA2_0==10) ) {
                    	        alt2=1;
                    	    }
                    	    switch (alt2) {
                    	        case 1 :
                    	            // InternalTaxonomy.g:159:4: otherlv_3= ','
                    	            {
                    	            otherlv_3=(Token)match(input,10,FollowSets000.FOLLOW_5); 

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

                    otherlv_4=(Token)match(input,11,FollowSets000.FOLLOW_2); 

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


 

    
    private static class FollowSets000 {
        public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
        public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
        public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000012L});
        public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000202L});
        public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000C10L});
    }


}