export type Command = {
  id: string;
  label: string;
  action: string;
};

/**
 * Add your own commands here.
 *
 * Example:
 * { id: "cut", label: "Cut", action: "CMD+X" }
 *
 * The starter receiver only logs commands. This keeps the MVP safe
 * until you choose your preferred Mac automation layer.
 */
export const mappings: Command[] = [
  { id: "play_pause", label: "Play / Pause", action: "SPACE" },
  { id: "cut", label: "Cut", action: "CMD+B" },
  { id: "undo", label: "Undo", action: "CMD+Z" }
];
