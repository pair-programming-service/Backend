package com.pair.website.repository.querydsl;

import com.pair.website.domain.PairBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static com.pair.website.domain.QPairBoard.pairBoard;
import static com.pair.website.domain.QBoardLanguage.boardLanguage;
import static org.springframework.util.StringUtils.hasText;


public class PairBoardRepositoryCustomImpl extends QuerydslRepositorySupport implements PairBoardRepositoryCustom{

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    public PairBoardRepositoryCustomImpl() {
        super(PairBoard.class);
    }


    @Override
    public Page<PairBoard> findDynamicQuery(Pageable pageable, String keyword,String category, Boolean cLanguage, Boolean cSharp, Boolean cPlusPlus, Boolean javaScript, Boolean java, Boolean python, Boolean nodeJs, Boolean typeScript) {
        QueryResults<PairBoard> query =  jpaQueryFactory.select(pairBoard)
                .from(pairBoard)
                .leftJoin(boardLanguage).on(pairBoard.id.eq(boardLanguage.pairBoard.id))
                .where(searchCondition(keyword),
                        categoryEqual(category),
                        cLanguageEqual(cLanguage),
                        cSharpEqual(cSharp),
                        cPlusPlusEqual(cPlusPlus),
                        javaScriptEqual(javaScript),
                        javaEqual(java),
                        pythonEqual(python),
                        nodeJsEqual(nodeJs),
                        typeScriptEqual(typeScript))
                .orderBy(pairBoard.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(query.getResults(),pageable,query.getTotal());

    }

    private BooleanBuilder searchCondition(String keyword){
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(hasText(keyword)) {
            booleanBuilder.or(pairBoard.title.contains(keyword));
            booleanBuilder.or(pairBoard.content.contains(keyword));
        }
        return booleanBuilder;
    }

    private BooleanExpression categoryEqual(String category){
        if(category.equals("")) return null;
        return pairBoard.category.eq(category);
    }
    private BooleanExpression cLanguageEqual(Boolean cLanguage){
        if(cLanguage == Boolean.FALSE) return null;
        return boardLanguage.cLanguage.eq(true);
    }
    private BooleanExpression cSharpEqual(Boolean cSharp){
        if(cSharp == Boolean.FALSE) return null;
        return boardLanguage.cSharp.eq(true);
    }
    private BooleanExpression cPlusPlusEqual(Boolean cPlusPlus){
        if(cPlusPlus == Boolean.FALSE) return null;
        return boardLanguage.cPlusPlus.eq(true);
    }
    private BooleanExpression javaScriptEqual(Boolean javaScript){
        if(javaScript == Boolean.FALSE) return null;
        return boardLanguage.javaScript.eq(true);
    }
    private BooleanExpression pythonEqual(Boolean python){
        if(python == Boolean.FALSE) return null;
        return boardLanguage.python.eq(true);
    }
    private BooleanExpression javaEqual(Boolean java){
        if(java == Boolean.FALSE) return null;
        return boardLanguage.java.eq(true);
    }
    private BooleanExpression nodeJsEqual(Boolean nodeJs){
        if(nodeJs == Boolean.FALSE) return null;
        return boardLanguage.nodeJs.eq(true);
    }
    private BooleanExpression typeScriptEqual(Boolean typeScript){
        if(typeScript == Boolean.FALSE) return null;
        return boardLanguage.typeScript.eq(true);
    }
}
