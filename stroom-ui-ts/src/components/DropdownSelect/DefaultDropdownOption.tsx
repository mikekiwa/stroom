import * as React from "react";
import { withProps } from "recompose";

import { DropdownOptionProps } from "./DropdownOptionTypes";

export interface AddedProps {
  className: string;
}

export interface EnhancedProps extends DropdownOptionProps, AddedProps {}

const enhance = withProps<AddedProps, DropdownOptionProps>(
  ({ inFocus }: DropdownOptionProps) => {
    let classNames = ["hoverable"];

    if (inFocus) classNames.push("inFocus");

    return {
      className: classNames.join(" ")
    };
  }
);

let DefaultDropdownOption = ({ option, className, onClick }: EnhancedProps) => (
  <div className={className} onClick={onClick}>
    {option.text}
  </div>
);

export const Wrapped = enhance(DefaultDropdownOption);

let DefaultDropdownOption2 = ({
  option,
  inFocus,
  onClick
}: DropdownOptionProps) => (
  <div className={`hoverable ${inFocus ? "inFocus" : ""}`} onClick={onClick}>
    {option.text}
  </div>
);
export default DefaultDropdownOption2;