package de.tudresden.slr.model.ide.contentassist.antlr.internal;

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.DFA;
import de.tudresden.slr.model.services.TaxanomyGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTaxanomyParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_ID", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'Model'", "'{'", "'}'", "'dimensions'", "','", "'Term'", "'subclasses'"
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

    	public void setGrammarAccess(TaxanomyGrammarAccess grammarAccess) {
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
    // InternalTaxanomy.g:53:1: entryRuleModel : ruleModel EOF ;
    public final void entryRuleModel() throws RecognitionException {
        try {
            // InternalTaxanomy.g:54:1: ( ruleModel EOF )
            // InternalTaxanomy.g:55:1: ruleModel EOF
            {
             before(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            ruleModel();

            state._fsp--;

             after(grammarAccess.getModelRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalTaxanomy.g:62:1: ruleModel : ( ( rule__Model__Group__0 ) ) ;
    public final void ruleModel() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:66:2: ( ( ( rule__Model__Group__0 ) ) )
            // InternalTaxanomy.g:67:2: ( ( rule__Model__Group__0 ) )
            {
            // InternalTaxanomy.g:67:2: ( ( rule__Model__Group__0 ) )
            // InternalTaxanomy.g:68:3: ( rule__Model__Group__0 )
            {
             before(grammarAccess.getModelAccess().getGroup()); 
            // InternalTaxanomy.g:69:3: ( rule__Model__Group__0 )
            // InternalTaxanomy.g:69:4: rule__Model__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Model__Group__0();

            state._fsp--;


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
    // InternalTaxanomy.g:78:1: entryRuleTerm : ruleTerm EOF ;
    public final void entryRuleTerm() throws RecognitionException {
        try {
            // InternalTaxanomy.g:79:1: ( ruleTerm EOF )
            // InternalTaxanomy.g:80:1: ruleTerm EOF
            {
             before(grammarAccess.getTermRule()); 
            pushFollow(FOLLOW_1);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getTermRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalTaxanomy.g:87:1: ruleTerm : ( ( rule__Term__Group__0 ) ) ;
    public final void ruleTerm() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:91:2: ( ( ( rule__Term__Group__0 ) ) )
            // InternalTaxanomy.g:92:2: ( ( rule__Term__Group__0 ) )
            {
            // InternalTaxanomy.g:92:2: ( ( rule__Term__Group__0 ) )
            // InternalTaxanomy.g:93:3: ( rule__Term__Group__0 )
            {
             before(grammarAccess.getTermAccess().getGroup()); 
            // InternalTaxanomy.g:94:3: ( rule__Term__Group__0 )
            // InternalTaxanomy.g:94:4: rule__Term__Group__0
            {
            pushFollow(FOLLOW_2);
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


    // $ANTLR start "entryRuleEString"
    // InternalTaxanomy.g:103:1: entryRuleEString : ruleEString EOF ;
    public final void entryRuleEString() throws RecognitionException {
        try {
            // InternalTaxanomy.g:104:1: ( ruleEString EOF )
            // InternalTaxanomy.g:105:1: ruleEString EOF
            {
             before(grammarAccess.getEStringRule()); 
            pushFollow(FOLLOW_1);
            ruleEString();

            state._fsp--;

             after(grammarAccess.getEStringRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // $ANTLR end "entryRuleEString"


    // $ANTLR start "ruleEString"
    // InternalTaxanomy.g:112:1: ruleEString : ( ( rule__EString__Alternatives ) ) ;
    public final void ruleEString() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:116:2: ( ( ( rule__EString__Alternatives ) ) )
            // InternalTaxanomy.g:117:2: ( ( rule__EString__Alternatives ) )
            {
            // InternalTaxanomy.g:117:2: ( ( rule__EString__Alternatives ) )
            // InternalTaxanomy.g:118:3: ( rule__EString__Alternatives )
            {
             before(grammarAccess.getEStringAccess().getAlternatives()); 
            // InternalTaxanomy.g:119:3: ( rule__EString__Alternatives )
            // InternalTaxanomy.g:119:4: rule__EString__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__EString__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getEStringAccess().getAlternatives()); 

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
    // $ANTLR end "ruleEString"


    // $ANTLR start "rule__EString__Alternatives"
    // InternalTaxanomy.g:127:1: rule__EString__Alternatives : ( ( RULE_STRING ) | ( RULE_ID ) );
    public final void rule__EString__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:131:1: ( ( RULE_STRING ) | ( RULE_ID ) )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==RULE_STRING) ) {
                alt1=1;
            }
            else if ( (LA1_0==RULE_ID) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // InternalTaxanomy.g:132:2: ( RULE_STRING )
                    {
                    // InternalTaxanomy.g:132:2: ( RULE_STRING )
                    // InternalTaxanomy.g:133:3: RULE_STRING
                    {
                     before(grammarAccess.getEStringAccess().getSTRINGTerminalRuleCall_0()); 
                    match(input,RULE_STRING,FOLLOW_2); 
                     after(grammarAccess.getEStringAccess().getSTRINGTerminalRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalTaxanomy.g:138:2: ( RULE_ID )
                    {
                    // InternalTaxanomy.g:138:2: ( RULE_ID )
                    // InternalTaxanomy.g:139:3: RULE_ID
                    {
                     before(grammarAccess.getEStringAccess().getIDTerminalRuleCall_1()); 
                    match(input,RULE_ID,FOLLOW_2); 
                     after(grammarAccess.getEStringAccess().getIDTerminalRuleCall_1()); 

                    }


                    }
                    break;

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
    // $ANTLR end "rule__EString__Alternatives"


    // $ANTLR start "rule__Model__Group__0"
    // InternalTaxanomy.g:148:1: rule__Model__Group__0 : rule__Model__Group__0__Impl rule__Model__Group__1 ;
    public final void rule__Model__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:152:1: ( rule__Model__Group__0__Impl rule__Model__Group__1 )
            // InternalTaxanomy.g:153:2: rule__Model__Group__0__Impl rule__Model__Group__1
            {
            pushFollow(FOLLOW_3);
            rule__Model__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalTaxanomy.g:160:1: rule__Model__Group__0__Impl : ( () ) ;
    public final void rule__Model__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:164:1: ( ( () ) )
            // InternalTaxanomy.g:165:1: ( () )
            {
            // InternalTaxanomy.g:165:1: ( () )
            // InternalTaxanomy.g:166:2: ()
            {
             before(grammarAccess.getModelAccess().getModelAction_0()); 
            // InternalTaxanomy.g:167:2: ()
            // InternalTaxanomy.g:167:3: 
            {
            }

             after(grammarAccess.getModelAccess().getModelAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__Group__0__Impl"


    // $ANTLR start "rule__Model__Group__1"
    // InternalTaxanomy.g:175:1: rule__Model__Group__1 : rule__Model__Group__1__Impl rule__Model__Group__2 ;
    public final void rule__Model__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:179:1: ( rule__Model__Group__1__Impl rule__Model__Group__2 )
            // InternalTaxanomy.g:180:2: rule__Model__Group__1__Impl rule__Model__Group__2
            {
            pushFollow(FOLLOW_4);
            rule__Model__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group__2();

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
    // InternalTaxanomy.g:187:1: rule__Model__Group__1__Impl : ( 'Model' ) ;
    public final void rule__Model__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:191:1: ( ( 'Model' ) )
            // InternalTaxanomy.g:192:1: ( 'Model' )
            {
            // InternalTaxanomy.g:192:1: ( 'Model' )
            // InternalTaxanomy.g:193:2: 'Model'
            {
             before(grammarAccess.getModelAccess().getModelKeyword_1()); 
            match(input,11,FOLLOW_2); 
             after(grammarAccess.getModelAccess().getModelKeyword_1()); 

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


    // $ANTLR start "rule__Model__Group__2"
    // InternalTaxanomy.g:202:1: rule__Model__Group__2 : rule__Model__Group__2__Impl rule__Model__Group__3 ;
    public final void rule__Model__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:206:1: ( rule__Model__Group__2__Impl rule__Model__Group__3 )
            // InternalTaxanomy.g:207:2: rule__Model__Group__2__Impl rule__Model__Group__3
            {
            pushFollow(FOLLOW_5);
            rule__Model__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group__3();

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
    // $ANTLR end "rule__Model__Group__2"


    // $ANTLR start "rule__Model__Group__2__Impl"
    // InternalTaxanomy.g:214:1: rule__Model__Group__2__Impl : ( '{' ) ;
    public final void rule__Model__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:218:1: ( ( '{' ) )
            // InternalTaxanomy.g:219:1: ( '{' )
            {
            // InternalTaxanomy.g:219:1: ( '{' )
            // InternalTaxanomy.g:220:2: '{'
            {
             before(grammarAccess.getModelAccess().getLeftCurlyBracketKeyword_2()); 
            match(input,12,FOLLOW_2); 
             after(grammarAccess.getModelAccess().getLeftCurlyBracketKeyword_2()); 

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
    // $ANTLR end "rule__Model__Group__2__Impl"


    // $ANTLR start "rule__Model__Group__3"
    // InternalTaxanomy.g:229:1: rule__Model__Group__3 : rule__Model__Group__3__Impl rule__Model__Group__4 ;
    public final void rule__Model__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:233:1: ( rule__Model__Group__3__Impl rule__Model__Group__4 )
            // InternalTaxanomy.g:234:2: rule__Model__Group__3__Impl rule__Model__Group__4
            {
            pushFollow(FOLLOW_5);
            rule__Model__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group__4();

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
    // $ANTLR end "rule__Model__Group__3"


    // $ANTLR start "rule__Model__Group__3__Impl"
    // InternalTaxanomy.g:241:1: rule__Model__Group__3__Impl : ( ( rule__Model__Group_3__0 )? ) ;
    public final void rule__Model__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:245:1: ( ( ( rule__Model__Group_3__0 )? ) )
            // InternalTaxanomy.g:246:1: ( ( rule__Model__Group_3__0 )? )
            {
            // InternalTaxanomy.g:246:1: ( ( rule__Model__Group_3__0 )? )
            // InternalTaxanomy.g:247:2: ( rule__Model__Group_3__0 )?
            {
             before(grammarAccess.getModelAccess().getGroup_3()); 
            // InternalTaxanomy.g:248:2: ( rule__Model__Group_3__0 )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==14) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // InternalTaxanomy.g:248:3: rule__Model__Group_3__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Model__Group_3__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModelAccess().getGroup_3()); 

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
    // $ANTLR end "rule__Model__Group__3__Impl"


    // $ANTLR start "rule__Model__Group__4"
    // InternalTaxanomy.g:256:1: rule__Model__Group__4 : rule__Model__Group__4__Impl ;
    public final void rule__Model__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:260:1: ( rule__Model__Group__4__Impl )
            // InternalTaxanomy.g:261:2: rule__Model__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Model__Group__4__Impl();

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
    // $ANTLR end "rule__Model__Group__4"


    // $ANTLR start "rule__Model__Group__4__Impl"
    // InternalTaxanomy.g:267:1: rule__Model__Group__4__Impl : ( '}' ) ;
    public final void rule__Model__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:271:1: ( ( '}' ) )
            // InternalTaxanomy.g:272:1: ( '}' )
            {
            // InternalTaxanomy.g:272:1: ( '}' )
            // InternalTaxanomy.g:273:2: '}'
            {
             before(grammarAccess.getModelAccess().getRightCurlyBracketKeyword_4()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getModelAccess().getRightCurlyBracketKeyword_4()); 

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
    // $ANTLR end "rule__Model__Group__4__Impl"


    // $ANTLR start "rule__Model__Group_3__0"
    // InternalTaxanomy.g:283:1: rule__Model__Group_3__0 : rule__Model__Group_3__0__Impl rule__Model__Group_3__1 ;
    public final void rule__Model__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:287:1: ( rule__Model__Group_3__0__Impl rule__Model__Group_3__1 )
            // InternalTaxanomy.g:288:2: rule__Model__Group_3__0__Impl rule__Model__Group_3__1
            {
            pushFollow(FOLLOW_4);
            rule__Model__Group_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group_3__1();

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
    // $ANTLR end "rule__Model__Group_3__0"


    // $ANTLR start "rule__Model__Group_3__0__Impl"
    // InternalTaxanomy.g:295:1: rule__Model__Group_3__0__Impl : ( 'dimensions' ) ;
    public final void rule__Model__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:299:1: ( ( 'dimensions' ) )
            // InternalTaxanomy.g:300:1: ( 'dimensions' )
            {
            // InternalTaxanomy.g:300:1: ( 'dimensions' )
            // InternalTaxanomy.g:301:2: 'dimensions'
            {
             before(grammarAccess.getModelAccess().getDimensionsKeyword_3_0()); 
            match(input,14,FOLLOW_2); 
             after(grammarAccess.getModelAccess().getDimensionsKeyword_3_0()); 

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
    // $ANTLR end "rule__Model__Group_3__0__Impl"


    // $ANTLR start "rule__Model__Group_3__1"
    // InternalTaxanomy.g:310:1: rule__Model__Group_3__1 : rule__Model__Group_3__1__Impl rule__Model__Group_3__2 ;
    public final void rule__Model__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:314:1: ( rule__Model__Group_3__1__Impl rule__Model__Group_3__2 )
            // InternalTaxanomy.g:315:2: rule__Model__Group_3__1__Impl rule__Model__Group_3__2
            {
            pushFollow(FOLLOW_6);
            rule__Model__Group_3__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group_3__2();

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
    // $ANTLR end "rule__Model__Group_3__1"


    // $ANTLR start "rule__Model__Group_3__1__Impl"
    // InternalTaxanomy.g:322:1: rule__Model__Group_3__1__Impl : ( '{' ) ;
    public final void rule__Model__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:326:1: ( ( '{' ) )
            // InternalTaxanomy.g:327:1: ( '{' )
            {
            // InternalTaxanomy.g:327:1: ( '{' )
            // InternalTaxanomy.g:328:2: '{'
            {
             before(grammarAccess.getModelAccess().getLeftCurlyBracketKeyword_3_1()); 
            match(input,12,FOLLOW_2); 
             after(grammarAccess.getModelAccess().getLeftCurlyBracketKeyword_3_1()); 

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
    // $ANTLR end "rule__Model__Group_3__1__Impl"


    // $ANTLR start "rule__Model__Group_3__2"
    // InternalTaxanomy.g:337:1: rule__Model__Group_3__2 : rule__Model__Group_3__2__Impl rule__Model__Group_3__3 ;
    public final void rule__Model__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:341:1: ( rule__Model__Group_3__2__Impl rule__Model__Group_3__3 )
            // InternalTaxanomy.g:342:2: rule__Model__Group_3__2__Impl rule__Model__Group_3__3
            {
            pushFollow(FOLLOW_7);
            rule__Model__Group_3__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group_3__3();

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
    // $ANTLR end "rule__Model__Group_3__2"


    // $ANTLR start "rule__Model__Group_3__2__Impl"
    // InternalTaxanomy.g:349:1: rule__Model__Group_3__2__Impl : ( ( rule__Model__DimensionsAssignment_3_2 ) ) ;
    public final void rule__Model__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:353:1: ( ( ( rule__Model__DimensionsAssignment_3_2 ) ) )
            // InternalTaxanomy.g:354:1: ( ( rule__Model__DimensionsAssignment_3_2 ) )
            {
            // InternalTaxanomy.g:354:1: ( ( rule__Model__DimensionsAssignment_3_2 ) )
            // InternalTaxanomy.g:355:2: ( rule__Model__DimensionsAssignment_3_2 )
            {
             before(grammarAccess.getModelAccess().getDimensionsAssignment_3_2()); 
            // InternalTaxanomy.g:356:2: ( rule__Model__DimensionsAssignment_3_2 )
            // InternalTaxanomy.g:356:3: rule__Model__DimensionsAssignment_3_2
            {
            pushFollow(FOLLOW_2);
            rule__Model__DimensionsAssignment_3_2();

            state._fsp--;


            }

             after(grammarAccess.getModelAccess().getDimensionsAssignment_3_2()); 

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
    // $ANTLR end "rule__Model__Group_3__2__Impl"


    // $ANTLR start "rule__Model__Group_3__3"
    // InternalTaxanomy.g:364:1: rule__Model__Group_3__3 : rule__Model__Group_3__3__Impl rule__Model__Group_3__4 ;
    public final void rule__Model__Group_3__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:368:1: ( rule__Model__Group_3__3__Impl rule__Model__Group_3__4 )
            // InternalTaxanomy.g:369:2: rule__Model__Group_3__3__Impl rule__Model__Group_3__4
            {
            pushFollow(FOLLOW_7);
            rule__Model__Group_3__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group_3__4();

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
    // $ANTLR end "rule__Model__Group_3__3"


    // $ANTLR start "rule__Model__Group_3__3__Impl"
    // InternalTaxanomy.g:376:1: rule__Model__Group_3__3__Impl : ( ( rule__Model__Group_3_3__0 )* ) ;
    public final void rule__Model__Group_3__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:380:1: ( ( ( rule__Model__Group_3_3__0 )* ) )
            // InternalTaxanomy.g:381:1: ( ( rule__Model__Group_3_3__0 )* )
            {
            // InternalTaxanomy.g:381:1: ( ( rule__Model__Group_3_3__0 )* )
            // InternalTaxanomy.g:382:2: ( rule__Model__Group_3_3__0 )*
            {
             before(grammarAccess.getModelAccess().getGroup_3_3()); 
            // InternalTaxanomy.g:383:2: ( rule__Model__Group_3_3__0 )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==15) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalTaxanomy.g:383:3: rule__Model__Group_3_3__0
            	    {
            	    pushFollow(FOLLOW_8);
            	    rule__Model__Group_3_3__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

             after(grammarAccess.getModelAccess().getGroup_3_3()); 

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
    // $ANTLR end "rule__Model__Group_3__3__Impl"


    // $ANTLR start "rule__Model__Group_3__4"
    // InternalTaxanomy.g:391:1: rule__Model__Group_3__4 : rule__Model__Group_3__4__Impl ;
    public final void rule__Model__Group_3__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:395:1: ( rule__Model__Group_3__4__Impl )
            // InternalTaxanomy.g:396:2: rule__Model__Group_3__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Model__Group_3__4__Impl();

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
    // $ANTLR end "rule__Model__Group_3__4"


    // $ANTLR start "rule__Model__Group_3__4__Impl"
    // InternalTaxanomy.g:402:1: rule__Model__Group_3__4__Impl : ( '}' ) ;
    public final void rule__Model__Group_3__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:406:1: ( ( '}' ) )
            // InternalTaxanomy.g:407:1: ( '}' )
            {
            // InternalTaxanomy.g:407:1: ( '}' )
            // InternalTaxanomy.g:408:2: '}'
            {
             before(grammarAccess.getModelAccess().getRightCurlyBracketKeyword_3_4()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getModelAccess().getRightCurlyBracketKeyword_3_4()); 

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
    // $ANTLR end "rule__Model__Group_3__4__Impl"


    // $ANTLR start "rule__Model__Group_3_3__0"
    // InternalTaxanomy.g:418:1: rule__Model__Group_3_3__0 : rule__Model__Group_3_3__0__Impl rule__Model__Group_3_3__1 ;
    public final void rule__Model__Group_3_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:422:1: ( rule__Model__Group_3_3__0__Impl rule__Model__Group_3_3__1 )
            // InternalTaxanomy.g:423:2: rule__Model__Group_3_3__0__Impl rule__Model__Group_3_3__1
            {
            pushFollow(FOLLOW_6);
            rule__Model__Group_3_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group_3_3__1();

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
    // $ANTLR end "rule__Model__Group_3_3__0"


    // $ANTLR start "rule__Model__Group_3_3__0__Impl"
    // InternalTaxanomy.g:430:1: rule__Model__Group_3_3__0__Impl : ( ',' ) ;
    public final void rule__Model__Group_3_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:434:1: ( ( ',' ) )
            // InternalTaxanomy.g:435:1: ( ',' )
            {
            // InternalTaxanomy.g:435:1: ( ',' )
            // InternalTaxanomy.g:436:2: ','
            {
             before(grammarAccess.getModelAccess().getCommaKeyword_3_3_0()); 
            match(input,15,FOLLOW_2); 
             after(grammarAccess.getModelAccess().getCommaKeyword_3_3_0()); 

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
    // $ANTLR end "rule__Model__Group_3_3__0__Impl"


    // $ANTLR start "rule__Model__Group_3_3__1"
    // InternalTaxanomy.g:445:1: rule__Model__Group_3_3__1 : rule__Model__Group_3_3__1__Impl ;
    public final void rule__Model__Group_3_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:449:1: ( rule__Model__Group_3_3__1__Impl )
            // InternalTaxanomy.g:450:2: rule__Model__Group_3_3__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Model__Group_3_3__1__Impl();

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
    // $ANTLR end "rule__Model__Group_3_3__1"


    // $ANTLR start "rule__Model__Group_3_3__1__Impl"
    // InternalTaxanomy.g:456:1: rule__Model__Group_3_3__1__Impl : ( ( rule__Model__DimensionsAssignment_3_3_1 ) ) ;
    public final void rule__Model__Group_3_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:460:1: ( ( ( rule__Model__DimensionsAssignment_3_3_1 ) ) )
            // InternalTaxanomy.g:461:1: ( ( rule__Model__DimensionsAssignment_3_3_1 ) )
            {
            // InternalTaxanomy.g:461:1: ( ( rule__Model__DimensionsAssignment_3_3_1 ) )
            // InternalTaxanomy.g:462:2: ( rule__Model__DimensionsAssignment_3_3_1 )
            {
             before(grammarAccess.getModelAccess().getDimensionsAssignment_3_3_1()); 
            // InternalTaxanomy.g:463:2: ( rule__Model__DimensionsAssignment_3_3_1 )
            // InternalTaxanomy.g:463:3: rule__Model__DimensionsAssignment_3_3_1
            {
            pushFollow(FOLLOW_2);
            rule__Model__DimensionsAssignment_3_3_1();

            state._fsp--;


            }

             after(grammarAccess.getModelAccess().getDimensionsAssignment_3_3_1()); 

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
    // $ANTLR end "rule__Model__Group_3_3__1__Impl"


    // $ANTLR start "rule__Term__Group__0"
    // InternalTaxanomy.g:472:1: rule__Term__Group__0 : rule__Term__Group__0__Impl rule__Term__Group__1 ;
    public final void rule__Term__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:476:1: ( rule__Term__Group__0__Impl rule__Term__Group__1 )
            // InternalTaxanomy.g:477:2: rule__Term__Group__0__Impl rule__Term__Group__1
            {
            pushFollow(FOLLOW_6);
            rule__Term__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalTaxanomy.g:484:1: rule__Term__Group__0__Impl : ( () ) ;
    public final void rule__Term__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:488:1: ( ( () ) )
            // InternalTaxanomy.g:489:1: ( () )
            {
            // InternalTaxanomy.g:489:1: ( () )
            // InternalTaxanomy.g:490:2: ()
            {
             before(grammarAccess.getTermAccess().getTermAction_0()); 
            // InternalTaxanomy.g:491:2: ()
            // InternalTaxanomy.g:491:3: 
            {
            }

             after(grammarAccess.getTermAccess().getTermAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Term__Group__0__Impl"


    // $ANTLR start "rule__Term__Group__1"
    // InternalTaxanomy.g:499:1: rule__Term__Group__1 : rule__Term__Group__1__Impl rule__Term__Group__2 ;
    public final void rule__Term__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:503:1: ( rule__Term__Group__1__Impl rule__Term__Group__2 )
            // InternalTaxanomy.g:504:2: rule__Term__Group__1__Impl rule__Term__Group__2
            {
            pushFollow(FOLLOW_9);
            rule__Term__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group__2();

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
    // InternalTaxanomy.g:511:1: rule__Term__Group__1__Impl : ( 'Term' ) ;
    public final void rule__Term__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:515:1: ( ( 'Term' ) )
            // InternalTaxanomy.g:516:1: ( 'Term' )
            {
            // InternalTaxanomy.g:516:1: ( 'Term' )
            // InternalTaxanomy.g:517:2: 'Term'
            {
             before(grammarAccess.getTermAccess().getTermKeyword_1()); 
            match(input,16,FOLLOW_2); 
             after(grammarAccess.getTermAccess().getTermKeyword_1()); 

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


    // $ANTLR start "rule__Term__Group__2"
    // InternalTaxanomy.g:526:1: rule__Term__Group__2 : rule__Term__Group__2__Impl rule__Term__Group__3 ;
    public final void rule__Term__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:530:1: ( rule__Term__Group__2__Impl rule__Term__Group__3 )
            // InternalTaxanomy.g:531:2: rule__Term__Group__2__Impl rule__Term__Group__3
            {
            pushFollow(FOLLOW_4);
            rule__Term__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group__3();

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
    // $ANTLR end "rule__Term__Group__2"


    // $ANTLR start "rule__Term__Group__2__Impl"
    // InternalTaxanomy.g:538:1: rule__Term__Group__2__Impl : ( ( rule__Term__NameAssignment_2 ) ) ;
    public final void rule__Term__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:542:1: ( ( ( rule__Term__NameAssignment_2 ) ) )
            // InternalTaxanomy.g:543:1: ( ( rule__Term__NameAssignment_2 ) )
            {
            // InternalTaxanomy.g:543:1: ( ( rule__Term__NameAssignment_2 ) )
            // InternalTaxanomy.g:544:2: ( rule__Term__NameAssignment_2 )
            {
             before(grammarAccess.getTermAccess().getNameAssignment_2()); 
            // InternalTaxanomy.g:545:2: ( rule__Term__NameAssignment_2 )
            // InternalTaxanomy.g:545:3: rule__Term__NameAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__Term__NameAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getTermAccess().getNameAssignment_2()); 

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
    // $ANTLR end "rule__Term__Group__2__Impl"


    // $ANTLR start "rule__Term__Group__3"
    // InternalTaxanomy.g:553:1: rule__Term__Group__3 : rule__Term__Group__3__Impl rule__Term__Group__4 ;
    public final void rule__Term__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:557:1: ( rule__Term__Group__3__Impl rule__Term__Group__4 )
            // InternalTaxanomy.g:558:2: rule__Term__Group__3__Impl rule__Term__Group__4
            {
            pushFollow(FOLLOW_10);
            rule__Term__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group__4();

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
    // $ANTLR end "rule__Term__Group__3"


    // $ANTLR start "rule__Term__Group__3__Impl"
    // InternalTaxanomy.g:565:1: rule__Term__Group__3__Impl : ( '{' ) ;
    public final void rule__Term__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:569:1: ( ( '{' ) )
            // InternalTaxanomy.g:570:1: ( '{' )
            {
            // InternalTaxanomy.g:570:1: ( '{' )
            // InternalTaxanomy.g:571:2: '{'
            {
             before(grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_3()); 
            match(input,12,FOLLOW_2); 
             after(grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_3()); 

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
    // $ANTLR end "rule__Term__Group__3__Impl"


    // $ANTLR start "rule__Term__Group__4"
    // InternalTaxanomy.g:580:1: rule__Term__Group__4 : rule__Term__Group__4__Impl rule__Term__Group__5 ;
    public final void rule__Term__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:584:1: ( rule__Term__Group__4__Impl rule__Term__Group__5 )
            // InternalTaxanomy.g:585:2: rule__Term__Group__4__Impl rule__Term__Group__5
            {
            pushFollow(FOLLOW_10);
            rule__Term__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group__5();

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
    // $ANTLR end "rule__Term__Group__4"


    // $ANTLR start "rule__Term__Group__4__Impl"
    // InternalTaxanomy.g:592:1: rule__Term__Group__4__Impl : ( ( rule__Term__Group_4__0 )? ) ;
    public final void rule__Term__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:596:1: ( ( ( rule__Term__Group_4__0 )? ) )
            // InternalTaxanomy.g:597:1: ( ( rule__Term__Group_4__0 )? )
            {
            // InternalTaxanomy.g:597:1: ( ( rule__Term__Group_4__0 )? )
            // InternalTaxanomy.g:598:2: ( rule__Term__Group_4__0 )?
            {
             before(grammarAccess.getTermAccess().getGroup_4()); 
            // InternalTaxanomy.g:599:2: ( rule__Term__Group_4__0 )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==17) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalTaxanomy.g:599:3: rule__Term__Group_4__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Term__Group_4__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getTermAccess().getGroup_4()); 

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
    // $ANTLR end "rule__Term__Group__4__Impl"


    // $ANTLR start "rule__Term__Group__5"
    // InternalTaxanomy.g:607:1: rule__Term__Group__5 : rule__Term__Group__5__Impl ;
    public final void rule__Term__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:611:1: ( rule__Term__Group__5__Impl )
            // InternalTaxanomy.g:612:2: rule__Term__Group__5__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Term__Group__5__Impl();

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
    // $ANTLR end "rule__Term__Group__5"


    // $ANTLR start "rule__Term__Group__5__Impl"
    // InternalTaxanomy.g:618:1: rule__Term__Group__5__Impl : ( '}' ) ;
    public final void rule__Term__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:622:1: ( ( '}' ) )
            // InternalTaxanomy.g:623:1: ( '}' )
            {
            // InternalTaxanomy.g:623:1: ( '}' )
            // InternalTaxanomy.g:624:2: '}'
            {
             before(grammarAccess.getTermAccess().getRightCurlyBracketKeyword_5()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getTermAccess().getRightCurlyBracketKeyword_5()); 

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
    // $ANTLR end "rule__Term__Group__5__Impl"


    // $ANTLR start "rule__Term__Group_4__0"
    // InternalTaxanomy.g:634:1: rule__Term__Group_4__0 : rule__Term__Group_4__0__Impl rule__Term__Group_4__1 ;
    public final void rule__Term__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:638:1: ( rule__Term__Group_4__0__Impl rule__Term__Group_4__1 )
            // InternalTaxanomy.g:639:2: rule__Term__Group_4__0__Impl rule__Term__Group_4__1
            {
            pushFollow(FOLLOW_4);
            rule__Term__Group_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group_4__1();

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
    // $ANTLR end "rule__Term__Group_4__0"


    // $ANTLR start "rule__Term__Group_4__0__Impl"
    // InternalTaxanomy.g:646:1: rule__Term__Group_4__0__Impl : ( 'subclasses' ) ;
    public final void rule__Term__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:650:1: ( ( 'subclasses' ) )
            // InternalTaxanomy.g:651:1: ( 'subclasses' )
            {
            // InternalTaxanomy.g:651:1: ( 'subclasses' )
            // InternalTaxanomy.g:652:2: 'subclasses'
            {
             before(grammarAccess.getTermAccess().getSubclassesKeyword_4_0()); 
            match(input,17,FOLLOW_2); 
             after(grammarAccess.getTermAccess().getSubclassesKeyword_4_0()); 

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
    // $ANTLR end "rule__Term__Group_4__0__Impl"


    // $ANTLR start "rule__Term__Group_4__1"
    // InternalTaxanomy.g:661:1: rule__Term__Group_4__1 : rule__Term__Group_4__1__Impl rule__Term__Group_4__2 ;
    public final void rule__Term__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:665:1: ( rule__Term__Group_4__1__Impl rule__Term__Group_4__2 )
            // InternalTaxanomy.g:666:2: rule__Term__Group_4__1__Impl rule__Term__Group_4__2
            {
            pushFollow(FOLLOW_6);
            rule__Term__Group_4__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group_4__2();

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
    // $ANTLR end "rule__Term__Group_4__1"


    // $ANTLR start "rule__Term__Group_4__1__Impl"
    // InternalTaxanomy.g:673:1: rule__Term__Group_4__1__Impl : ( '{' ) ;
    public final void rule__Term__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:677:1: ( ( '{' ) )
            // InternalTaxanomy.g:678:1: ( '{' )
            {
            // InternalTaxanomy.g:678:1: ( '{' )
            // InternalTaxanomy.g:679:2: '{'
            {
             before(grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_4_1()); 
            match(input,12,FOLLOW_2); 
             after(grammarAccess.getTermAccess().getLeftCurlyBracketKeyword_4_1()); 

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
    // $ANTLR end "rule__Term__Group_4__1__Impl"


    // $ANTLR start "rule__Term__Group_4__2"
    // InternalTaxanomy.g:688:1: rule__Term__Group_4__2 : rule__Term__Group_4__2__Impl rule__Term__Group_4__3 ;
    public final void rule__Term__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:692:1: ( rule__Term__Group_4__2__Impl rule__Term__Group_4__3 )
            // InternalTaxanomy.g:693:2: rule__Term__Group_4__2__Impl rule__Term__Group_4__3
            {
            pushFollow(FOLLOW_7);
            rule__Term__Group_4__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group_4__3();

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
    // $ANTLR end "rule__Term__Group_4__2"


    // $ANTLR start "rule__Term__Group_4__2__Impl"
    // InternalTaxanomy.g:700:1: rule__Term__Group_4__2__Impl : ( ( rule__Term__SubclassesAssignment_4_2 ) ) ;
    public final void rule__Term__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:704:1: ( ( ( rule__Term__SubclassesAssignment_4_2 ) ) )
            // InternalTaxanomy.g:705:1: ( ( rule__Term__SubclassesAssignment_4_2 ) )
            {
            // InternalTaxanomy.g:705:1: ( ( rule__Term__SubclassesAssignment_4_2 ) )
            // InternalTaxanomy.g:706:2: ( rule__Term__SubclassesAssignment_4_2 )
            {
             before(grammarAccess.getTermAccess().getSubclassesAssignment_4_2()); 
            // InternalTaxanomy.g:707:2: ( rule__Term__SubclassesAssignment_4_2 )
            // InternalTaxanomy.g:707:3: rule__Term__SubclassesAssignment_4_2
            {
            pushFollow(FOLLOW_2);
            rule__Term__SubclassesAssignment_4_2();

            state._fsp--;


            }

             after(grammarAccess.getTermAccess().getSubclassesAssignment_4_2()); 

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
    // $ANTLR end "rule__Term__Group_4__2__Impl"


    // $ANTLR start "rule__Term__Group_4__3"
    // InternalTaxanomy.g:715:1: rule__Term__Group_4__3 : rule__Term__Group_4__3__Impl rule__Term__Group_4__4 ;
    public final void rule__Term__Group_4__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:719:1: ( rule__Term__Group_4__3__Impl rule__Term__Group_4__4 )
            // InternalTaxanomy.g:720:2: rule__Term__Group_4__3__Impl rule__Term__Group_4__4
            {
            pushFollow(FOLLOW_7);
            rule__Term__Group_4__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group_4__4();

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
    // $ANTLR end "rule__Term__Group_4__3"


    // $ANTLR start "rule__Term__Group_4__3__Impl"
    // InternalTaxanomy.g:727:1: rule__Term__Group_4__3__Impl : ( ( rule__Term__Group_4_3__0 )* ) ;
    public final void rule__Term__Group_4__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:731:1: ( ( ( rule__Term__Group_4_3__0 )* ) )
            // InternalTaxanomy.g:732:1: ( ( rule__Term__Group_4_3__0 )* )
            {
            // InternalTaxanomy.g:732:1: ( ( rule__Term__Group_4_3__0 )* )
            // InternalTaxanomy.g:733:2: ( rule__Term__Group_4_3__0 )*
            {
             before(grammarAccess.getTermAccess().getGroup_4_3()); 
            // InternalTaxanomy.g:734:2: ( rule__Term__Group_4_3__0 )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==15) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalTaxanomy.g:734:3: rule__Term__Group_4_3__0
            	    {
            	    pushFollow(FOLLOW_8);
            	    rule__Term__Group_4_3__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

             after(grammarAccess.getTermAccess().getGroup_4_3()); 

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
    // $ANTLR end "rule__Term__Group_4__3__Impl"


    // $ANTLR start "rule__Term__Group_4__4"
    // InternalTaxanomy.g:742:1: rule__Term__Group_4__4 : rule__Term__Group_4__4__Impl ;
    public final void rule__Term__Group_4__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:746:1: ( rule__Term__Group_4__4__Impl )
            // InternalTaxanomy.g:747:2: rule__Term__Group_4__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Term__Group_4__4__Impl();

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
    // $ANTLR end "rule__Term__Group_4__4"


    // $ANTLR start "rule__Term__Group_4__4__Impl"
    // InternalTaxanomy.g:753:1: rule__Term__Group_4__4__Impl : ( '}' ) ;
    public final void rule__Term__Group_4__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:757:1: ( ( '}' ) )
            // InternalTaxanomy.g:758:1: ( '}' )
            {
            // InternalTaxanomy.g:758:1: ( '}' )
            // InternalTaxanomy.g:759:2: '}'
            {
             before(grammarAccess.getTermAccess().getRightCurlyBracketKeyword_4_4()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getTermAccess().getRightCurlyBracketKeyword_4_4()); 

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
    // $ANTLR end "rule__Term__Group_4__4__Impl"


    // $ANTLR start "rule__Term__Group_4_3__0"
    // InternalTaxanomy.g:769:1: rule__Term__Group_4_3__0 : rule__Term__Group_4_3__0__Impl rule__Term__Group_4_3__1 ;
    public final void rule__Term__Group_4_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:773:1: ( rule__Term__Group_4_3__0__Impl rule__Term__Group_4_3__1 )
            // InternalTaxanomy.g:774:2: rule__Term__Group_4_3__0__Impl rule__Term__Group_4_3__1
            {
            pushFollow(FOLLOW_6);
            rule__Term__Group_4_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Term__Group_4_3__1();

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
    // $ANTLR end "rule__Term__Group_4_3__0"


    // $ANTLR start "rule__Term__Group_4_3__0__Impl"
    // InternalTaxanomy.g:781:1: rule__Term__Group_4_3__0__Impl : ( ',' ) ;
    public final void rule__Term__Group_4_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:785:1: ( ( ',' ) )
            // InternalTaxanomy.g:786:1: ( ',' )
            {
            // InternalTaxanomy.g:786:1: ( ',' )
            // InternalTaxanomy.g:787:2: ','
            {
             before(grammarAccess.getTermAccess().getCommaKeyword_4_3_0()); 
            match(input,15,FOLLOW_2); 
             after(grammarAccess.getTermAccess().getCommaKeyword_4_3_0()); 

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
    // $ANTLR end "rule__Term__Group_4_3__0__Impl"


    // $ANTLR start "rule__Term__Group_4_3__1"
    // InternalTaxanomy.g:796:1: rule__Term__Group_4_3__1 : rule__Term__Group_4_3__1__Impl ;
    public final void rule__Term__Group_4_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:800:1: ( rule__Term__Group_4_3__1__Impl )
            // InternalTaxanomy.g:801:2: rule__Term__Group_4_3__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Term__Group_4_3__1__Impl();

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
    // $ANTLR end "rule__Term__Group_4_3__1"


    // $ANTLR start "rule__Term__Group_4_3__1__Impl"
    // InternalTaxanomy.g:807:1: rule__Term__Group_4_3__1__Impl : ( ( rule__Term__SubclassesAssignment_4_3_1 ) ) ;
    public final void rule__Term__Group_4_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:811:1: ( ( ( rule__Term__SubclassesAssignment_4_3_1 ) ) )
            // InternalTaxanomy.g:812:1: ( ( rule__Term__SubclassesAssignment_4_3_1 ) )
            {
            // InternalTaxanomy.g:812:1: ( ( rule__Term__SubclassesAssignment_4_3_1 ) )
            // InternalTaxanomy.g:813:2: ( rule__Term__SubclassesAssignment_4_3_1 )
            {
             before(grammarAccess.getTermAccess().getSubclassesAssignment_4_3_1()); 
            // InternalTaxanomy.g:814:2: ( rule__Term__SubclassesAssignment_4_3_1 )
            // InternalTaxanomy.g:814:3: rule__Term__SubclassesAssignment_4_3_1
            {
            pushFollow(FOLLOW_2);
            rule__Term__SubclassesAssignment_4_3_1();

            state._fsp--;


            }

             after(grammarAccess.getTermAccess().getSubclassesAssignment_4_3_1()); 

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
    // $ANTLR end "rule__Term__Group_4_3__1__Impl"


    // $ANTLR start "rule__Model__DimensionsAssignment_3_2"
    // InternalTaxanomy.g:823:1: rule__Model__DimensionsAssignment_3_2 : ( ruleTerm ) ;
    public final void rule__Model__DimensionsAssignment_3_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:827:1: ( ( ruleTerm ) )
            // InternalTaxanomy.g:828:2: ( ruleTerm )
            {
            // InternalTaxanomy.g:828:2: ( ruleTerm )
            // InternalTaxanomy.g:829:3: ruleTerm
            {
             before(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_3_2_0()); 
            pushFollow(FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_3_2_0()); 

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
    // $ANTLR end "rule__Model__DimensionsAssignment_3_2"


    // $ANTLR start "rule__Model__DimensionsAssignment_3_3_1"
    // InternalTaxanomy.g:838:1: rule__Model__DimensionsAssignment_3_3_1 : ( ruleTerm ) ;
    public final void rule__Model__DimensionsAssignment_3_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:842:1: ( ( ruleTerm ) )
            // InternalTaxanomy.g:843:2: ( ruleTerm )
            {
            // InternalTaxanomy.g:843:2: ( ruleTerm )
            // InternalTaxanomy.g:844:3: ruleTerm
            {
             before(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_3_3_1_0()); 
            pushFollow(FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getModelAccess().getDimensionsTermParserRuleCall_3_3_1_0()); 

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
    // $ANTLR end "rule__Model__DimensionsAssignment_3_3_1"


    // $ANTLR start "rule__Term__NameAssignment_2"
    // InternalTaxanomy.g:853:1: rule__Term__NameAssignment_2 : ( ruleEString ) ;
    public final void rule__Term__NameAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:857:1: ( ( ruleEString ) )
            // InternalTaxanomy.g:858:2: ( ruleEString )
            {
            // InternalTaxanomy.g:858:2: ( ruleEString )
            // InternalTaxanomy.g:859:3: ruleEString
            {
             before(grammarAccess.getTermAccess().getNameEStringParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
            ruleEString();

            state._fsp--;

             after(grammarAccess.getTermAccess().getNameEStringParserRuleCall_2_0()); 

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
    // $ANTLR end "rule__Term__NameAssignment_2"


    // $ANTLR start "rule__Term__SubclassesAssignment_4_2"
    // InternalTaxanomy.g:868:1: rule__Term__SubclassesAssignment_4_2 : ( ruleTerm ) ;
    public final void rule__Term__SubclassesAssignment_4_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:872:1: ( ( ruleTerm ) )
            // InternalTaxanomy.g:873:2: ( ruleTerm )
            {
            // InternalTaxanomy.g:873:2: ( ruleTerm )
            // InternalTaxanomy.g:874:3: ruleTerm
            {
             before(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_4_2_0()); 
            pushFollow(FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_4_2_0()); 

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
    // $ANTLR end "rule__Term__SubclassesAssignment_4_2"


    // $ANTLR start "rule__Term__SubclassesAssignment_4_3_1"
    // InternalTaxanomy.g:883:1: rule__Term__SubclassesAssignment_4_3_1 : ( ruleTerm ) ;
    public final void rule__Term__SubclassesAssignment_4_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTaxanomy.g:887:1: ( ( ruleTerm ) )
            // InternalTaxanomy.g:888:2: ( ruleTerm )
            {
            // InternalTaxanomy.g:888:2: ( ruleTerm )
            // InternalTaxanomy.g:889:3: ruleTerm
            {
             before(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_4_3_1_0()); 
            pushFollow(FOLLOW_2);
            ruleTerm();

            state._fsp--;

             after(grammarAccess.getTermAccess().getSubclassesTermParserRuleCall_4_3_1_0()); 

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
    // $ANTLR end "rule__Term__SubclassesAssignment_4_3_1"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x000000000000A000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000022000L});

}