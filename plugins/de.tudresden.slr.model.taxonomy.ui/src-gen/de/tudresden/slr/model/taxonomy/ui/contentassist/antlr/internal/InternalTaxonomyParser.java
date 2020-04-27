package de.tudresden.slr.model.taxonomy.ui.contentassist.antlr.internal; 

import org.antlr.runtime.BitSet;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;

import de.tudresden.slr.model.taxonomy.services.TaxonomyGrammarAccess;

@SuppressWarnings("all")
public class InternalTaxonomyParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_SL_COMMENT", "RULE_ML_COMMENT", "RULE_WS", "RULE_NEWLINE", "','", "'{'", "'}'"
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
     	
        public void setGrammarAccess(TaxonomyGrammarAccess grammarAccess) {
        	this.grammarAccess = grammarAccess;
        }
        
        @Override
        protected Grammar getGrammar() {
        	return grammarAccess.getGrammar();
        }
        
        @Override
        protected String getValueForTokenName(String tokenName) {
        	return tokenName;
        }




    // $ANTLR start "entryRuleModel"
    // InternalTaxonomy.g:60:1: entryRuleModel : ruleModel EOF ;
    public final void entryRuleModel() throws RecognitionException {
        try {
            // InternalTaxonomy.g:61:1: ( ruleModel EOF )
            // InternalTaxonomy.g:62:1: ruleModel EOF
            {
             before(grammarAccess.getModelRule()); 
            pushFollow(FollowSets000.FOLLOW_1);
            ruleModel();

            state._fsp--;

             after(grammarAccess.getModelRule()); 
            match(input,EOF,FollowSets000.FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalTaxonomy.g:69:1: ruleModel : ( ( rule__Model__Group__0 )? ) ;
    public final void ruleModel() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:73:2: ( ( ( rule__Model__Group__0 )? ) )
            // InternalTaxonomy.g:74:1: ( ( rule__Model__Group__0 )? )
            {
            // InternalTaxonomy.g:74:1: ( ( rule__Model__Group__0 )? )
            // InternalTaxonomy.g:75:1: ( rule__Model__Group__0 )?
            {
             before(grammarAccess.getModelAccess().getGroup()); 
            // InternalTaxonomy.g:76:1: ( rule__Model__Group__0 )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==RULE_ID) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // InternalTaxonomy.g:76:2: rule__Model__Group__0
                    {
                    pushFollow(FollowSets000.FOLLOW_2);
                    rule__Model__Group__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModelAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleTerm"
    // InternalTaxonomy.g:88:1: entryRuleTerm : ruleTerm EOF ;
    public final void entryRuleTerm() throws RecognitionException {
        try {
            // InternalTaxonomy.g:89:1: ( ruleTerm EOF )
            // InternalTaxonomy.g:90:1: ruleTerm EOF
            {
             before(grammarAccess.getTermRule()); 
            pushFollow(FollowSets000.FOLLOW_1);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getTermRule()); 
            match(input,EOF,FollowSets000.FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleTerm"


    // $ANTLR start "ruleTerm"
    // InternalTaxonomy.g:97:1: ruleTerm : ( ( rule__Term__Group__0 ) ) ;
    public final void ruleTerm() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:101:2: ( ( ( rule__Term__Group__0 ) ) )
            // InternalTaxonomy.g:102:1: ( ( rule__Term__Group__0 ) )
            {
            // InternalTaxonomy.g:102:1: ( ( rule__Term__Group__0 ) )
            // InternalTaxonomy.g:103:1: ( rule__Term__Group__0 )
            {
             before(grammarAccess.getTermAccess().getGroup()); 
            // InternalTaxonomy.g:104:1: ( rule__Term__Group__0 )
            // InternalTaxonomy.g:104:2: rule__Term__Group__0
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getTermAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleTerm"


    // $ANTLR start "rule__Model__Group__0"
    // InternalTaxonomy.g:118:1: rule__Model__Group__0 : rule__Model__Group__0__Impl rule__Model__Group__1 ;
    public final void rule__Model__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:122:1: ( rule__Model__Group__0__Impl rule__Model__Group__1 )
            // InternalTaxonomy.g:123:2: rule__Model__Group__0__Impl rule__Model__Group__1
            {
            pushFollow(FollowSets000.FOLLOW_3);
            rule__Model__Group__0__Impl();

            state._fsp--;

            pushFollow(FollowSets000.FOLLOW_2);
            rule__Model__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__Group__0"


    // $ANTLR start "rule__Model__Group__0__Impl"
    // InternalTaxonomy.g:130:1: rule__Model__Group__0__Impl : ( ( rule__Model__DimensionsAssignment_0 ) ) ;
    public final void rule__Model__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:134:1: ( ( ( rule__Model__DimensionsAssignment_0 ) ) )
            // InternalTaxonomy.g:135:1: ( ( rule__Model__DimensionsAssignment_0 ) )
            {
            // InternalTaxonomy.g:135:1: ( ( rule__Model__DimensionsAssignment_0 ) )
            // InternalTaxonomy.g:136:1: ( rule__Model__DimensionsAssignment_0 )
            {
             before(grammarAccess.getModelAccess().getDimensionsAssignment_0()); 
            // InternalTaxonomy.g:137:1: ( rule__Model__DimensionsAssignment_0 )
            // InternalTaxonomy.g:137:2: rule__Model__DimensionsAssignment_0
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Model__DimensionsAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getModelAccess().getDimensionsAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__Group__0__Impl"


    // $ANTLR start "rule__Model__Group__1"
    // InternalTaxonomy.g:147:1: rule__Model__Group__1 : rule__Model__Group__1__Impl ;
    public final void rule__Model__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:151:1: ( rule__Model__Group__1__Impl )
            // InternalTaxonomy.g:152:2: rule__Model__Group__1__Impl
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Model__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__Group__1"


    // $ANTLR start "rule__Model__Group__1__Impl"
    // InternalTaxonomy.g:158:1: rule__Model__Group__1__Impl : ( ( rule__Model__Group_1__0 )* ) ;
    public final void rule__Model__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:162:1: ( ( ( rule__Model__Group_1__0 )* ) )
            // InternalTaxonomy.g:163:1: ( ( rule__Model__Group_1__0 )* )
            {
            // InternalTaxonomy.g:163:1: ( ( rule__Model__Group_1__0 )* )
            // InternalTaxonomy.g:164:1: ( rule__Model__Group_1__0 )*
            {
             before(grammarAccess.getModelAccess().getGroup_1()); 
            // InternalTaxonomy.g:165:1: ( rule__Model__Group_1__0 )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==9) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalTaxonomy.g:165:2: rule__Model__Group_1__0
            	    {
            	    pushFollow(FollowSets000.FOLLOW_4);
            	    rule__Model__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

             after(grammarAccess.getModelAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__Group__1__Impl"


    // $ANTLR start "rule__Model__Group_1__0"
    // InternalTaxonomy.g:179:1: rule__Model__Group_1__0 : rule__Model__Group_1__0__Impl rule__Model__Group_1__1 ;
    public final void rule__Model__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:183:1: ( rule__Model__Group_1__0__Impl rule__Model__Group_1__1 )
            // InternalTaxonomy.g:184:2: rule__Model__Group_1__0__Impl rule__Model__Group_1__1
            {
            pushFollow(FollowSets000.FOLLOW_5);
            rule__Model__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FollowSets000.FOLLOW_2);
            rule__Model__Group_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__Group_1__0"


    // $ANTLR start "rule__Model__Group_1__0__Impl"
    // InternalTaxonomy.g:191:1: rule__Model__Group_1__0__Impl : ( ',' ) ;
    public final void rule__Model__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:195:1: ( ( ',' ) )
            // InternalTaxonomy.g:196:1: ( ',' )
            {
            // InternalTaxonomy.g:196:1: ( ',' )
            // InternalTaxonomy.g:197:1: ','
            {
             before(grammarAccess.getModelAccess().getCommaKeyword_1_0()); 
            match(input,9,FollowSets000.FOLLOW_2); 
             after(grammarAccess.getModelAccess().getCommaKeyword_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__Group_1__0__Impl"


    // $ANTLR start "rule__Model__Group_1__1"
    // InternalTaxonomy.g:210:1: rule__Model__Group_1__1 : rule__Model__Group_1__1__Impl ;
    public final void rule__Model__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:214:1: ( rule__Model__Group_1__1__Impl )
            // InternalTaxonomy.g:215:2: rule__Model__Group_1__1__Impl
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Model__Group_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__Group_1__1"


    // $ANTLR start "rule__Model__Group_1__1__Impl"
    // InternalTaxonomy.g:221:1: rule__Model__Group_1__1__Impl : ( ( rule__Model__DimensionsAssignment_1_1 ) ) ;
    public final void rule__Model__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:225:1: ( ( ( rule__Model__DimensionsAssignment_1_1 ) ) )
            // InternalTaxonomy.g:226:1: ( ( rule__Model__DimensionsAssignment_1_1 ) )
            {
            // InternalTaxonomy.g:226:1: ( ( rule__Model__DimensionsAssignment_1_1 ) )
            // InternalTaxonomy.g:227:1: ( rule__Model__DimensionsAssignment_1_1 )
            {
             before(grammarAccess.getModelAccess().getDimensionsAssignment_1_1()); 
            // InternalTaxonomy.g:228:1: ( rule__Model__DimensionsAssignment_1_1 )
            // InternalTaxonomy.g:228:2: rule__Model__DimensionsAssignment_1_1
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Model__DimensionsAssignment_1_1();

            state._fsp--;


            }

             after(grammarAccess.getModelAccess().getDimensionsAssignment_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__Group_1__1__Impl"


    // $ANTLR start "rule__Term__Group__0"
    // InternalTaxonomy.g:242:1: rule__Term__Group__0 : rule__Term__Group__0__Impl rule__Term__Group__1 ;
    public final void rule__Term__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:246:1: ( rule__Term__Group__0__Impl rule__Term__Group__1 )
            // InternalTaxonomy.g:247:2: rule__Term__Group__0__Impl rule__Term__Group__1
            {
            pushFollow(FollowSets000.FOLLOW_6);
            rule__Term__Group__0__Impl();

            state._fsp--;

            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group__0"


    // $ANTLR start "rule__Term__Group__0__Impl"
    // InternalTaxonomy.g:254:1: rule__Term__Group__0__Impl : ( ( rule__Term__NameAssignment_0 ) ) ;
    public final void rule__Term__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:258:1: ( ( ( rule__Term__NameAssignment_0 ) ) )
            // InternalTaxonomy.g:259:1: ( ( rule__Term__NameAssignment_0 ) )
            {
            // InternalTaxonomy.g:259:1: ( ( rule__Term__NameAssignment_0 ) )
            // InternalTaxonomy.g:260:1: ( rule__Term__NameAssignment_0 )
            {
             before(grammarAccess.getTermAccess().getNameAssignment_0()); 
            // InternalTaxonomy.g:261:1: ( rule__Term__NameAssignment_0 )
            // InternalTaxonomy.g:261:2: rule__Term__NameAssignment_0
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getTermAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group__0__Impl"


    // $ANTLR start "rule__Term__Group__1"
    // InternalTaxonomy.g:271:1: rule__Term__Group__1 : rule__Term__Group__1__Impl ;
    public final void rule__Term__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:275:1: ( rule__Term__Group__1__Impl )
            // InternalTaxonomy.g:276:2: rule__Term__Group__1__Impl
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group__1"


    // $ANTLR start "rule__Term__Group__1__Impl"
    // InternalTaxonomy.g:282:1: rule__Term__Group__1__Impl : ( ( rule__Term__Group_1__0 )? ) ;
    public final void rule__Term__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:286:1: ( ( ( rule__Term__Group_1__0 )? ) )
            // InternalTaxonomy.g:287:1: ( ( rule__Term__Group_1__0 )? )
            {
            // InternalTaxonomy.g:287:1: ( ( rule__Term__Group_1__0 )? )
            // InternalTaxonomy.g:288:1: ( rule__Term__Group_1__0 )?
            {
             before(grammarAccess.getTermAccess().getGroup_1()); 
            // InternalTaxonomy.g:289:1: ( rule__Term__Group_1__0 )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==10) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalTaxonomy.g:289:2: rule__Term__Group_1__0
                    {
                    pushFollow(FollowSets000.FOLLOW_2);
                    rule__Term__Group_1__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getTermAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group__1__Impl"


    // $ANTLR start "rule__Term__Group_1__0"
    // InternalTaxonomy.g:303:1: rule__Term__Group_1__0 : rule__Term__Group_1__0__Impl rule__Term__Group_1__1 ;
    public final void rule__Term__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:307:1: ( rule__Term__Group_1__0__Impl rule__Term__Group_1__1 )
            // InternalTaxonomy.g:308:2: rule__Term__Group_1__0__Impl rule__Term__Group_1__1
            {
            pushFollow(FollowSets000.FOLLOW_7);
            rule__Term__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__Group_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__0"


    // $ANTLR start "rule__Term__Group_1__0__Impl"
    // InternalTaxonomy.g:315:1: rule__Term__Group_1__0__Impl : ( '{' ) ;
    public final void rule__Term__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:319:1: ( ( '{' ) )
            // InternalTaxonomy.g:320:1: ( '{' )
            {
            // InternalTaxonomy.g:320:1: ( '{' )
            // InternalTaxonomy.g:321:1: '{'
            {
             before(grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_1_0()); 
            match(input,10,FollowSets000.FOLLOW_2); 
             after(grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__0__Impl"


    // $ANTLR start "rule__Term__Group_1__1"
    // InternalTaxonomy.g:334:1: rule__Term__Group_1__1 : rule__Term__Group_1__1__Impl rule__Term__Group_1__2 ;
    public final void rule__Term__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:338:1: ( rule__Term__Group_1__1__Impl rule__Term__Group_1__2 )
            // InternalTaxonomy.g:339:2: rule__Term__Group_1__1__Impl rule__Term__Group_1__2
            {
            pushFollow(FollowSets000.FOLLOW_7);
            rule__Term__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__Group_1__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__1"


    // $ANTLR start "rule__Term__Group_1__1__Impl"
    // InternalTaxonomy.g:346:1: rule__Term__Group_1__1__Impl : ( ( rule__Term__Group_1_1__0 )? ) ;
    public final void rule__Term__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:350:1: ( ( ( rule__Term__Group_1_1__0 )? ) )
            // InternalTaxonomy.g:351:1: ( ( rule__Term__Group_1_1__0 )? )
            {
            // InternalTaxonomy.g:351:1: ( ( rule__Term__Group_1_1__0 )? )
            // InternalTaxonomy.g:352:1: ( rule__Term__Group_1_1__0 )?
            {
             before(grammarAccess.getTermAccess().getGroup_1_1()); 
            // InternalTaxonomy.g:353:1: ( rule__Term__Group_1_1__0 )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==RULE_ID) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalTaxonomy.g:353:2: rule__Term__Group_1_1__0
                    {
                    pushFollow(FollowSets000.FOLLOW_2);
                    rule__Term__Group_1_1__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getTermAccess().getGroup_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__1__Impl"


    // $ANTLR start "rule__Term__Group_1__2"
    // InternalTaxonomy.g:363:1: rule__Term__Group_1__2 : rule__Term__Group_1__2__Impl ;
    public final void rule__Term__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:367:1: ( rule__Term__Group_1__2__Impl )
            // InternalTaxonomy.g:368:2: rule__Term__Group_1__2__Impl
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__Group_1__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__2"


    // $ANTLR start "rule__Term__Group_1__2__Impl"
    // InternalTaxonomy.g:374:1: rule__Term__Group_1__2__Impl : ( '}' ) ;
    public final void rule__Term__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:378:1: ( ( '}' ) )
            // InternalTaxonomy.g:379:1: ( '}' )
            {
            // InternalTaxonomy.g:379:1: ( '}' )
            // InternalTaxonomy.g:380:1: '}'
            {
             before(grammarAccess.getTermAccess().getRightCurlyBracketKeyword_1_2()); 
            match(input,11,FollowSets000.FOLLOW_2); 
             after(grammarAccess.getTermAccess().getRightCurlyBracketKeyword_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1__2__Impl"


    // $ANTLR start "rule__Term__Group_1_1__0"
    // InternalTaxonomy.g:399:1: rule__Term__Group_1_1__0 : rule__Term__Group_1_1__0__Impl rule__Term__Group_1_1__1 ;
    public final void rule__Term__Group_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:403:1: ( rule__Term__Group_1_1__0__Impl rule__Term__Group_1_1__1 )
            // InternalTaxonomy.g:404:2: rule__Term__Group_1_1__0__Impl rule__Term__Group_1_1__1
            {
            pushFollow(FollowSets000.FOLLOW_3);
            rule__Term__Group_1_1__0__Impl();

            state._fsp--;

            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__Group_1_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1__0"


    // $ANTLR start "rule__Term__Group_1_1__0__Impl"
    // InternalTaxonomy.g:411:1: rule__Term__Group_1_1__0__Impl : ( ( rule__Term__SubclassesAssignment_1_1_0 ) ) ;
    public final void rule__Term__Group_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:415:1: ( ( ( rule__Term__SubclassesAssignment_1_1_0 ) ) )
            // InternalTaxonomy.g:416:1: ( ( rule__Term__SubclassesAssignment_1_1_0 ) )
            {
            // InternalTaxonomy.g:416:1: ( ( rule__Term__SubclassesAssignment_1_1_0 ) )
            // InternalTaxonomy.g:417:1: ( rule__Term__SubclassesAssignment_1_1_0 )
            {
             before(grammarAccess.getTermAccess().getSubclassesAssignment_1_1_0()); 
            // InternalTaxonomy.g:418:1: ( rule__Term__SubclassesAssignment_1_1_0 )
            // InternalTaxonomy.g:418:2: rule__Term__SubclassesAssignment_1_1_0
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__SubclassesAssignment_1_1_0();

            state._fsp--;


            }

             after(grammarAccess.getTermAccess().getSubclassesAssignment_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1__0__Impl"


    // $ANTLR start "rule__Term__Group_1_1__1"
    // InternalTaxonomy.g:428:1: rule__Term__Group_1_1__1 : rule__Term__Group_1_1__1__Impl ;
    public final void rule__Term__Group_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:432:1: ( rule__Term__Group_1_1__1__Impl )
            // InternalTaxonomy.g:433:2: rule__Term__Group_1_1__1__Impl
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__Group_1_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1__1"


    // $ANTLR start "rule__Term__Group_1_1__1__Impl"
    // InternalTaxonomy.g:439:1: rule__Term__Group_1_1__1__Impl : ( ( rule__Term__Group_1_1_1__0 )* ) ;
    public final void rule__Term__Group_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:443:1: ( ( ( rule__Term__Group_1_1_1__0 )* ) )
            // InternalTaxonomy.g:444:1: ( ( rule__Term__Group_1_1_1__0 )* )
            {
            // InternalTaxonomy.g:444:1: ( ( rule__Term__Group_1_1_1__0 )* )
            // InternalTaxonomy.g:445:1: ( rule__Term__Group_1_1_1__0 )*
            {
             before(grammarAccess.getTermAccess().getGroup_1_1_1()); 
            // InternalTaxonomy.g:446:1: ( rule__Term__Group_1_1_1__0 )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==9) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalTaxonomy.g:446:2: rule__Term__Group_1_1_1__0
            	    {
            	    pushFollow(FollowSets000.FOLLOW_4);
            	    rule__Term__Group_1_1_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

             after(grammarAccess.getTermAccess().getGroup_1_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1__1__Impl"


    // $ANTLR start "rule__Term__Group_1_1_1__0"
    // InternalTaxonomy.g:460:1: rule__Term__Group_1_1_1__0 : rule__Term__Group_1_1_1__0__Impl rule__Term__Group_1_1_1__1 ;
    public final void rule__Term__Group_1_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:464:1: ( rule__Term__Group_1_1_1__0__Impl rule__Term__Group_1_1_1__1 )
            // InternalTaxonomy.g:465:2: rule__Term__Group_1_1_1__0__Impl rule__Term__Group_1_1_1__1
            {
            pushFollow(FollowSets000.FOLLOW_5);
            rule__Term__Group_1_1_1__0__Impl();

            state._fsp--;

            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__Group_1_1_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1_1__0"


    // $ANTLR start "rule__Term__Group_1_1_1__0__Impl"
    // InternalTaxonomy.g:472:1: rule__Term__Group_1_1_1__0__Impl : ( ',' ) ;
    public final void rule__Term__Group_1_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:476:1: ( ( ',' ) )
            // InternalTaxonomy.g:477:1: ( ',' )
            {
            // InternalTaxonomy.g:477:1: ( ',' )
            // InternalTaxonomy.g:478:1: ','
            {
             before(grammarAccess.getTermAccess().getCommaKeyword_1_1_1_0()); 
            match(input,9,FollowSets000.FOLLOW_2); 
             after(grammarAccess.getTermAccess().getCommaKeyword_1_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1_1__0__Impl"


    // $ANTLR start "rule__Term__Group_1_1_1__1"
    // InternalTaxonomy.g:491:1: rule__Term__Group_1_1_1__1 : rule__Term__Group_1_1_1__1__Impl ;
    public final void rule__Term__Group_1_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:495:1: ( rule__Term__Group_1_1_1__1__Impl )
            // InternalTaxonomy.g:496:2: rule__Term__Group_1_1_1__1__Impl
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__Group_1_1_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1_1__1"


    // $ANTLR start "rule__Term__Group_1_1_1__1__Impl"
    // InternalTaxonomy.g:502:1: rule__Term__Group_1_1_1__1__Impl : ( ( rule__Term__SubclassesAssignment_1_1_1_1 ) ) ;
    public final void rule__Term__Group_1_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:506:1: ( ( ( rule__Term__SubclassesAssignment_1_1_1_1 ) ) )
            // InternalTaxonomy.g:507:1: ( ( rule__Term__SubclassesAssignment_1_1_1_1 ) )
            {
            // InternalTaxonomy.g:507:1: ( ( rule__Term__SubclassesAssignment_1_1_1_1 ) )
            // InternalTaxonomy.g:508:1: ( rule__Term__SubclassesAssignment_1_1_1_1 )
            {
             before(grammarAccess.getTermAccess().getSubclassesAssignment_1_1_1_1()); 
            // InternalTaxonomy.g:509:1: ( rule__Term__SubclassesAssignment_1_1_1_1 )
            // InternalTaxonomy.g:509:2: rule__Term__SubclassesAssignment_1_1_1_1
            {
            pushFollow(FollowSets000.FOLLOW_2);
            rule__Term__SubclassesAssignment_1_1_1_1();

            state._fsp--;


            }

             after(grammarAccess.getTermAccess().getSubclassesAssignment_1_1_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group_1_1_1__1__Impl"


    // $ANTLR start "rule__Model__DimensionsAssignment_0"
    // InternalTaxonomy.g:524:1: rule__Model__DimensionsAssignment_0 : ( ruleTerm ) ;
    public final void rule__Model__DimensionsAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:528:1: ( ( ruleTerm ) )
            // InternalTaxonomy.g:529:1: ( ruleTerm )
            {
            // InternalTaxonomy.g:529:1: ( ruleTerm )
            // InternalTaxonomy.g:530:1: ruleTerm
            {
             before(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_0_0()); 
            pushFollow(FollowSets000.FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__DimensionsAssignment_0"


    // $ANTLR start "rule__Model__DimensionsAssignment_1_1"
    // InternalTaxonomy.g:539:1: rule__Model__DimensionsAssignment_1_1 : ( ruleTerm ) ;
    public final void rule__Model__DimensionsAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:543:1: ( ( ruleTerm ) )
            // InternalTaxonomy.g:544:1: ( ruleTerm )
            {
            // InternalTaxonomy.g:544:1: ( ruleTerm )
            // InternalTaxonomy.g:545:1: ruleTerm
            {
             before(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_1_1_0()); 
            pushFollow(FollowSets000.FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__DimensionsAssignment_1_1"


    // $ANTLR start "rule__Term__NameAssignment_0"
    // InternalTaxonomy.g:554:1: rule__Term__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Term__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:558:1: ( ( RULE_ID ) )
            // InternalTaxonomy.g:559:1: ( RULE_ID )
            {
            // InternalTaxonomy.g:559:1: ( RULE_ID )
            // InternalTaxonomy.g:560:1: RULE_ID
            {
             before(grammarAccess.getTermAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FollowSets000.FOLLOW_2); 
             after(grammarAccess.getTermAccess().getNameIDTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__NameAssignment_0"


    // $ANTLR start "rule__Term__SubclassesAssignment_1_1_0"
    // InternalTaxonomy.g:569:1: rule__Term__SubclassesAssignment_1_1_0 : ( ruleTerm ) ;
    public final void rule__Term__SubclassesAssignment_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:573:1: ( ( ruleTerm ) )
            // InternalTaxonomy.g:574:1: ( ruleTerm )
            {
            // InternalTaxonomy.g:574:1: ( ruleTerm )
            // InternalTaxonomy.g:575:1: ruleTerm
            {
             before(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_1_1_0_0()); 
            pushFollow(FollowSets000.FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_1_1_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__SubclassesAssignment_1_1_0"


    // $ANTLR start "rule__Term__SubclassesAssignment_1_1_1_1"
    // InternalTaxonomy.g:584:1: rule__Term__SubclassesAssignment_1_1_1_1 : ( ruleTerm ) ;
    public final void rule__Term__SubclassesAssignment_1_1_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:588:1: ( ( ruleTerm ) )
            // InternalTaxonomy.g:589:1: ( ruleTerm )
            {
            // InternalTaxonomy.g:589:1: ( ruleTerm )
            // InternalTaxonomy.g:590:1: ruleTerm
            {
             before(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_1_1_1_1_0()); 
            pushFollow(FollowSets000.FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_1_1_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__SubclassesAssignment_1_1_1_1"

    // Delegated rules


 

    
    private static class FollowSets000 {
        public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
        public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
        public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000200L});
        public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000202L});
        public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
        public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000400L});
        public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000810L});
    }


}