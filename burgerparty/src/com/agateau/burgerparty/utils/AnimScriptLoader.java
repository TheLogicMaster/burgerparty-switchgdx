package com.agateau.burgerparty.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

public class AnimScriptLoader {
	private Map<String, InstructionDefinition> mInstructionDefinitionMap = new HashMap<String, InstructionDefinition>();

	public AnimScriptLoader() {
		registerAction("moveTo",
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Width),
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Height),
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Duration, 0),
				new InterpolationArgumentDefinition(Interpolation.linear)
		);
		registerAction("moveBy",
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Width),
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Height),
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Duration, 0),
				new InterpolationArgumentDefinition(Interpolation.linear)
		);
		registerAction("rotateTo",
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Scalar),
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Duration, 0),
				new InterpolationArgumentDefinition(Interpolation.linear)
		);
		registerAction("scaleTo",
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Scalar),
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Scalar),
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Duration, 0),
				new InterpolationArgumentDefinition(Interpolation.linear)
		);
		registerAction("sizeTo",
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Width),
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Height),
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Duration, 0),
				new InterpolationArgumentDefinition(Interpolation.linear)
		);
		registerAction("alpha",
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Scalar),
				new FloatArgumentDefinition(FloatArgumentDefinition.Domain.Duration, 0),
				new InterpolationArgumentDefinition(Interpolation.linear)
		);
		mInstructionDefinitionMap.put("parallel", new ParallelInstructionDefinition(this));
		mInstructionDefinitionMap.put("repeat", new RepeatInstructionDefinition(this));
	}

	public AnimScript load(String definition) {
		Reader reader = new StringReader(definition);
		try {
			return load(reader);
		} catch (IOException e) {
			Gdx.app.error("AnimScript", "Failed to parse `" + definition + "`");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public AnimScript load(Reader reader) throws IOException {
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		tokenizer.eolIsSignificant(true);
		tokenizer.slashSlashComments(true);
		tokenizer.slashStarComments(true);
		tokenizer.parseNumbers();
		Array<Instruction> lst = tokenize(tokenizer, null);
		return new AnimScript(lst);
	}

	public Array<Instruction> tokenize(StreamTokenizer tokenizer, String end) throws IOException {
		Array<Instruction> lst = new Array<Instruction>();
		do {
			while (tokenizer.nextToken() == StreamTokenizer.TT_EOL) {
			}
			if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
				break;
			}
			assert(tokenizer.ttype == StreamTokenizer.TT_WORD);
			String cmd = tokenizer.sval;
			assert(cmd != null);
			if (end != null && cmd.equals(end)) {
				break;
			}
			InstructionDefinition def = mInstructionDefinitionMap.get(cmd);
			assert(def != null);
			Instruction instruction = def.parse(tokenizer);
			lst.add(instruction);
		} while (tokenizer.ttype != StreamTokenizer.TT_EOF);
		return lst;
	}

	public void registerStaticMethod(String name, Class<?> methodClass, String methodName, ArgumentDefinition<?>... types) {
		Method method = getMethod(methodClass, methodName, types);
		mInstructionDefinitionMap.put(name, new BasicInstructionDefinition(method, types));
	}

	public void registerMemberMethod(String name, Object object, String methodName, ArgumentDefinition<?>... types) {
		Method method = getMethod(object.getClass(), methodName, types);
		mInstructionDefinitionMap.put(name, new BasicInstructionDefinition(object, method, types));
	}

	private static Method getMethod(Class<?> methodClass, String name, ArgumentDefinition<?>... types) {
		Class<?> args[] = new Class<?>[types.length];
		for (int idx = 0; idx < types.length; ++idx) {
			args[idx] = types[idx].javaType;
		}
		try {
			return methodClass.getDeclaredMethod(name, args);
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
			throw new RuntimeException();
		} catch (SecurityException e1) {
			e1.printStackTrace();
			throw new RuntimeException();
		}
	}

	private void registerAction(String name, ArgumentDefinition<?>... types) {
		registerStaticMethod(name, Actions.class, name, types);
	}
}
