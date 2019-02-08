package io.realmarket.propeler.service.util;

import org.modelmapper.AbstractCondition;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ModelMapperBlankString extends ModelMapper {

  // string blank condition: https://github.com/modelmapper/modelmapper/issues/319
  private Condition<Object,Object> getCondition() {
    return new AbstractCondition<Object, Object>() {
      @Override
      public boolean applies(MappingContext<Object, Object> context) {
        if(context.getSource() instanceof String) {
          return !StringUtils.isEmpty(context.getSource());
        } else {
          return context.getSource() != null;
        }
      }
    };
  }

  ModelMapperBlankString() {
    this.getConfiguration().setPropertyCondition(getCondition());
  }
}
