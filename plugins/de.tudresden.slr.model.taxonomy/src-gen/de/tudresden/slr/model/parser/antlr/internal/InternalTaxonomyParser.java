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
import de.tudresden.slr.model.services.TaxonomyGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTaxonomyParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_ML_COMMENT", "RULE_WS", "RULE_NEWLINE", "'{'", "','", "'}'"
    };
    public static final int T__9=9;
    public static final int T__8=8;
    public static final int RULE_ID=4;
    public static final int RULE_WS=6;
    public static final int RULE_NEWLINE=7;
    public static final int RULE_ML_COMMENT=5;
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
    public String getGrammarFileName() { return "../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g"; }



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
    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:67:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:68:2: (iv_ruleModel= ruleModel EOF )
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:69:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_ruleModel_in_entryRuleModel75);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModel85); 

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
    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:76:1: ruleModel returns [EObject current=null] : ( (lv_dimensions_0_0= ruleTerm ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_dimensions_0_0 = null;


         enterRule(); 
            
        try {
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:79:28: ( ( (lv_dimensions_0_0= ruleTerm ) )* )
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:80:1: ( (lv_dimensions_0_0= ruleTerm ) )*
            {
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:80:1: ( (lv_dimensions_0_0= ruleTerm ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:81:1: (lv_dimensions_0_0= ruleTerm )
            	    {
            	    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:81:1: (lv_dimensions_0_0= ruleTerm )
            	    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:82:3: lv_dimensions_0_0= ruleTerm
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleTerm_in_ruleModel130);
            	    lv_dimensions_0_0=ruleTerm();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getModelRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"dimensions",
            	            		lv_dimensions_0_0, 
            	            		"Term");
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
    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:106:1: entryRuleTerm returns [EObject current=null] : iv_ruleTerm= ruleTerm EOF ;
    public final EObject entryRuleTerm() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTerm = null;


        try {
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:107:2: (iv_ruleTerm= ruleTerm EOF )
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:108:2: iv_ruleTerm= ruleTerm EOF
            {
             newCompositeNode(grammarAccess.getTermRule()); 
            pushFollow(FOLLOW_ruleTerm_in_entryRuleTerm166);
            iv_ruleTerm=ruleTerm();

            state._fsp--;

             current =iv_ruleTerm; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleTerm176); 

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
    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:115:1: ruleTerm returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? ) ;
    public final EObject ruleTerm() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_subclasses_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:118:28: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? ) )
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:119:1: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? )
            {
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:119:1: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )? )
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:119:2: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )?
            {
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:119:2: ( (lv_name_0_0= RULE_ID ) )
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:120:1: (lv_name_0_0= RULE_ID )
            {
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:120:1: (lv_name_0_0= RULE_ID )
            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:121:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleTerm218); 

            			newLeafNode(lv_name_0_0, grammarAccess.getTermAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getTermRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

            }


            }

            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:137:2: (otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==8) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:137:4: otherlv_1= '{' ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )* otherlv_4= '}'
                    {
                    otherlv_1=(Token)match(input,8,FOLLOW_8_in_ruleTerm236); 

                        	newLeafNode(otherlv_1, grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_1_0());
                        
                    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:141:1: ( ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )? )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0==RULE_ID) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:141:2: ( (lv_subclasses_2_0= ruleTerm ) ) (otherlv_3= ',' )?
                    	    {
                    	    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:141:2: ( (lv_subclasses_2_0= ruleTerm ) )
                    	    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:142:1: (lv_subclasses_2_0= ruleTerm )
                    	    {
                    	    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:142:1: (lv_subclasses_2_0= ruleTerm )
                    	    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:143:3: lv_subclasses_2_0= ruleTerm
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_1_1_0_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleTerm_in_ruleTerm258);
                    	    lv_subclasses_2_0=ruleTerm();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getTermRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"subclasses",
                    	            		lv_subclasses_2_0, 
                    	            		"Term");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }

                    	    // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:159:2: (otherlv_3= ',' )?
                    	    int alt2=2;
                    	    int LA2_0 = input.LA(1);

                    	    if ( (LA2_0==9) ) {
                    	        alt2=1;
                    	    }
                    	    switch (alt2) {
                    	        case 1 :
                    	            // ../de.tudresden.slr.model.taxonomy/src-gen/de/tudresden/slr/model/parser/antlr/internal/InternalTaxonomy.g:159:4: otherlv_3= ','
                    	            {
                    	            otherlv_3=(Token)match(input,9,FOLLOW_9_in_ruleTerm271); 

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

                    otherlv_4=(Token)match(input,10,FOLLOW_10_in_ruleTerm287); 

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


 

    public static final BitSet FOLLOW_ruleModel_in_entryRuleModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTerm_in_ruleModel130 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_ruleTerm_in_entryRuleTerm166 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTerm176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleTerm218 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_8_in_ruleTerm236 = new BitSet(new long[]{0x0000000000000610L});
    public static final BitSet FOLLOW_ruleTerm_in_ruleTerm258 = new BitSet(new long[]{0x0000000000000610L});
    public static final BitSet FOLLOW_9_in_ruleTerm271 = new BitSet(new long[]{0x0000000000000610L});
    public static final BitSet FOLLOW_10_in_ruleTerm287 = new BitSet(new long[]{0x0000000000000002L});

}