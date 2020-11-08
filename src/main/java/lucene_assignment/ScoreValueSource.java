package lucene_assignment;

import java.io.IOException;
import java.util.Objects;
import java.util.function.LongToDoubleFunction;

import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.DoubleValues;
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.IndexSearcher;

public class ScoreValueSource extends DoubleValuesSource {

	final String field;
	final LongToDoubleFunction decoder=(v) -> (double) v;

	public ScoreValueSource(String field) {
		this.field = field;
	}

	@Override
	public boolean isCacheable(LeafReaderContext ctx) {
		return DocValues.isCacheable(ctx, field);
	}

	@Override
	public DoubleValues getValues(LeafReaderContext ctx, DoubleValues scores) throws IOException {
		final NumericDocValues values = DocValues.getNumeric(ctx.reader(), field);
		return new DoubleValues() {
			@Override
			public double doubleValue() throws IOException {
				return decoder.applyAsDouble(Math.min(values.longValue()/1000,1));
			}

			@Override
			public boolean advanceExact(int target) throws IOException {
				return values.advanceExact(target);
			}
		};
	}

	@Override
	public boolean needsScores() {
		return false;
	}

	@Override
	public DoubleValuesSource rewrite(IndexSearcher reader) throws IOException {
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(field, decoder);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		return false;
	}

	@Override
	public String toString() {
		return "double(" + field + ")";
	}

}
