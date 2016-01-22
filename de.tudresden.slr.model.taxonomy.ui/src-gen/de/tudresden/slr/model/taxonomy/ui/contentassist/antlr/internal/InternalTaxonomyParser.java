package de.tudresden.slr.model.taxonomy.ui.contentassist.antlr.internal; 

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.DFA;
import de.tudresden.slr.model.taxonomy.services.TaxonomyGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTaxonomyParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_SL_COMMENT", "RULE_ML_COMMENT", "RULE_WS", "RULE_NEWLINE", "'{'", "'}'", "','"
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
    // InternalTaxonomy.g:69:1: ruleModel : ( ( rule__Model__DimensionsAssignment )* ) ;
    public final void ruleModel() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:73:2: ( ( ( rule__Model__DimensionsAssignment )* ) )
            // InternalTaxonomy.g:74:1: ( ( rule__Model__DimensionsAssignment )* )
            {
            // InternalTaxonomy.g:74:1: ( ( rule__Model__DimensionsAssignment )* )
            // InternalTaxonomy.g:75:1: ( rule__Model__DimensionsAssignment )*
            {
             before(grammarAccess.getModelAccess().getDimensionsAssignment()); 
            // InternalTaxonomy.g:76:1: ( rule__Model__DimensionsAssignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalTaxonomy.g:76:2: rule__Model__DimensionsAssignment
            	    {
            	    pushFollow(FollowSets000.FOLLOW_3);
            	    rule__Model__DimensionsAssignment();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             after(grammarAccess.getModelAccess().getDimensionsAssignment()); 

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


    // $ANTLR start "rule__Term__Group__0"
    // InternalTaxonomy.g:118:1: rule__Term__Group__0 : rule__Term__Group__0__Impl rule__Term__Group__1 ;
    public final void rule__Term__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:122:1: ( rule__Term__Group__0__Impl rule__Term__Group__1 )
            // InternalTaxonomy.g:123:2: rule__Term__Group__0__Impl rule__Term__Group__1
            {
            pushFollow(FollowSets000.FOLLOW_4);
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
    // InternalTaxonomy.g:130:1: rule__Term__Group__0__Impl : ( ( rule__Term__NameAssignment_0 ) ) ;
    public final void rule__Term__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:134:1: ( ( ( rule__Term__NameAssignment_0 ) ) )
            // InternalTaxonomy.g:135:1: ( ( rule__Term__NameAssignment_0 ) )
            {
            // InternalTaxonomy.g:135:1: ( ( rule__Term__NameAssignment_0 ) )
            // InternalTaxonomy.g:136:1: ( rule__Term__NameAssignment_0 )
            {
             before(grammarAccess.getTermAccess().getNameAssignment_0()); 
            // InternalTaxonomy.g:137:1: ( rule__Term__NameAssignment_0 )
            // InternalTaxonomy.g:137:2: rule__Term__NameAssignment_0
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
    // InternalTaxonomy.g:147:1: rule__Term__Group__1 : rule__Term__Group__1__Impl ;
    public final void rule__Term__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:151:1: ( rule__Term__Group__1__Impl )
            // InternalTaxonomy.g:152:2: rule__Term__Group__1__Impl
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
    // InternalTaxonomy.g:158:1: rule__Term__Group__1__Impl : ( ( rule__Term__Group_1__0 )? ) ;
    public final void rule__Term__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:162:1: ( ( ( rule__Term__Group_1__0 )? ) )
            // InternalTaxonomy.g:163:1: ( ( rule__Term__Group_1__0 )? )
            {
            // InternalTaxonomy.g:163:1: ( ( rule__Term__Group_1__0 )? )
            // InternalTaxonomy.g:164:1: ( rule__Term__Group_1__0 )?
            {
             before(grammarAccess.getTermAccess().getGroup_1()); 
            // InternalTaxonomy.g:165:1: ( rule__Term__Group_1__0 )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==9) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // InternalTaxonomy.g:165:2: rule__Term__Group_1__0
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
    // InternalTaxonomy.g:179:1: rule__Term__Group_1__0 : rule__Term__Group_1__0__Impl rule__Term__Group_1__1 ;
    public final void rule__Term__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:183:1: ( rule__Term__Group_1__0__Impl rule__Term__Group_1__1 )
            // InternalTaxonomy.g:184:2: rule__Term__Group_1__0__Impl rule__Term__Group_1__1
            {
            pushFollow(FollowSets000.FOLLOW_5);
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
    // InternalTaxonomy.g:191:1: rule__Term__Group_1__0__Impl : ( '{' ) ;
    public final void rule__Term__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:195:1: ( ( '{' ) )
            // InternalTaxonomy.g:196:1: ( '{' )
            {
            // InternalTaxonomy.g:196:1: ( '{' )
            // InternalTaxonomy.g:197:1: '{'
            {
             before(grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_1_0()); 
            match(input,9,FollowSets000.FOLLOW_2); 
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
    // InternalTaxonomy.g:210:1: rule__Term__Group_1__1 : rule__Term__Group_1__1__Impl rule__Term__Group_1__2 ;
    public final void rule__Term__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:214:1: ( rule__Term__Group_1__1__Impl rule__Term__Group_1__2 )
            // InternalTaxonomy.g:215:2: rule__Term__Group_1__1__Impl rule__Term__Group_1__2
            {
            pushFollow(FollowSets000.FOLLOW_5);
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
    // InternalTaxonomy.g:222:1: rule__Term__Group_1__1__Impl : ( ( rule__Term__Group_1_1__0 )* ) ;
    public final void rule__Term__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:226:1: ( ( ( rule__Term__Group_1_1__0 )* ) )
            // InternalTaxonomy.g:227:1: ( ( rule__Term__Group_1_1__0 )* )
            {
            // InternalTaxonomy.g:227:1: ( ( rule__Term__Group_1_1__0 )* )
            // InternalTaxonomy.g:228:1: ( rule__Term__Group_1_1__0 )*
            {
             before(grammarAccess.getTermAccess().getGroup_1_1()); 
            // InternalTaxonomy.g:229:1: ( rule__Term__Group_1_1__0 )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==RULE_ID) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalTaxonomy.g:229:2: rule__Term__Group_1_1__0
            	    {
            	    pushFollow(FollowSets000.FOLLOW_3);
            	    rule__Term__Group_1_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

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
    // InternalTaxonomy.g:239:1: rule__Term__Group_1__2 : rule__Term__Group_1__2__Impl ;
    public final void rule__Term__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:243:1: ( rule__Term__Group_1__2__Impl )
            // InternalTaxonomy.g:244:2: rule__Term__Group_1__2__Impl
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
    // InternalTaxonomy.g:250:1: rule__Term__Group_1__2__Impl : ( '}' ) ;
    public final void rule__Term__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:254:1: ( ( '}' ) )
            // InternalTaxonomy.g:255:1: ( '}' )
            {
            // InternalTaxonomy.g:255:1: ( '}' )
            // InternalTaxonomy.g:256:1: '}'
            {
             before(grammarAccess.getTermAccess().getRightCurlyBracketKeyword_1_2()); 
            match(input,10,FollowSets000.FOLLOW_2); 
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
    // InternalTaxonomy.g:275:1: rule__Term__Group_1_1__0 : rule__Term__Group_1_1__0__Impl rule__Term__Group_1_1__1 ;
    public final void rule__Term__Group_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:279:1: ( rule__Term__Group_1_1__0__Impl rule__Term__Group_1_1__1 )
            // InternalTaxonomy.g:280:2: rule__Term__Group_1_1__0__Impl rule__Term__Group_1_1__1
            {
            pushFollow(FollowSets000.FOLLOW_6);
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
    // InternalTaxonomy.g:287:1: rule__Term__Group_1_1__0__Impl : ( ( rule__Term__SubclassesAssignment_1_1_0 ) ) ;
    public final void rule__Term__Group_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:291:1: ( ( ( rule__Term__SubclassesAssignment_1_1_0 ) ) )
            // InternalTaxonomy.g:292:1: ( ( rule__Term__SubclassesAssignment_1_1_0 ) )
            {
            // InternalTaxonomy.g:292:1: ( ( rule__Term__SubclassesAssignment_1_1_0 ) )
            // InternalTaxonomy.g:293:1: ( rule__Term__SubclassesAssignment_1_1_0 )
            {
             before(grammarAccess.getTermAccess().getSubclassesAssignment_1_1_0()); 
            // InternalTaxonomy.g:294:1: ( rule__Term__SubclassesAssignment_1_1_0 )
            // InternalTaxonomy.g:294:2: rule__Term__SubclassesAssignment_1_1_0
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
    // InternalTaxonomy.g:304:1: rule__Term__Group_1_1__1 : rule__Term__Group_1_1__1__Impl ;
    public final void rule__Term__Group_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:308:1: ( rule__Term__Group_1_1__1__Impl )
            // InternalTaxonomy.g:309:2: rule__Term__Group_1_1__1__Impl
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
    // InternalTaxonomy.g:315:1: rule__Term__Group_1_1__1__Impl : ( ( ',' )? ) ;
    public final void rule__Term__Group_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:319:1: ( ( ( ',' )? ) )
            // InternalTaxonomy.g:320:1: ( ( ',' )? )
            {
            // InternalTaxonomy.g:320:1: ( ( ',' )? )
            // InternalTaxonomy.g:321:1: ( ',' )?
            {
             before(grammarAccess.getTermAccess().getCommaKeyword_1_1_1()); 
            // InternalTaxonomy.g:322:1: ( ',' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==11) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalTaxonomy.g:323:2: ','
                    {
                    match(input,11,FollowSets000.FOLLOW_2); 

                    }
                    break;

            }

             after(grammarAccess.getTermAccess().getCommaKeyword_1_1_1()); 

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


    // $ANTLR start "rule__Model__DimensionsAssignment"
    // InternalTaxonomy.g:339:1: rule__Model__DimensionsAssignment : ( ruleTerm ) ;
    public final void rule__Model__DimensionsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:343:1: ( ( ruleTerm ) )
            // InternalTaxonomy.g:344:1: ( ruleTerm )
            {
            // InternalTaxonomy.g:344:1: ( ruleTerm )
            // InternalTaxonomy.g:345:1: ruleTerm
            {
             before(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_0()); 
            pushFollow(FollowSets000.FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_0()); 

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
    // $ANTLR end "rule__Model__DimensionsAssignment"


    // $ANTLR start "rule__Term__NameAssignment_0"
    // InternalTaxonomy.g:354:1: rule__Term__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Term__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:358:1: ( ( RULE_ID ) )
            // InternalTaxonomy.g:359:1: ( RULE_ID )
            {
            // InternalTaxonomy.g:359:1: ( RULE_ID )
            // InternalTaxonomy.g:360:1: RULE_ID
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
    // InternalTaxonomy.g:369:1: rule__Term__SubclassesAssignment_1_1_0 : ( ruleTerm ) ;
    public final void rule__Term__SubclassesAssignment_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // InternalTaxonomy.g:373:1: ( ( ruleTerm ) )
            // InternalTaxonomy.g:374:1: ( ruleTerm )
            {
            // InternalTaxonomy.g:374:1: ( ruleTerm )
            // InternalTaxonomy.g:375:1: ruleTerm
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

    // Delegated rules


 

    
    private static class FollowSets000 {
        public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
        public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
        public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000012L});
        public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000200L});
        public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000410L});
        public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000800L});
    }


}